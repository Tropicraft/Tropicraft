package net.tropicraft.core.common.data;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class StructureConverter implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String STRUCTURE_DIRECTORY = "structure";

    private final String namespace;
    private final PackOutput output;
    private final Collection<Path> inputs;

    public StructureConverter(String namespace, PackOutput output, Collection<Path> inputs) {
        this.namespace = namespace;
        this.output = output;
        this.inputs = inputs;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Path outputStructureRoot = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(namespace).resolve(STRUCTURE_DIRECTORY);

        for (Path inputRoot : inputs) {
            Path structureRoot = inputRoot.resolve("data").resolve(namespace).resolve(STRUCTURE_DIRECTORY);
            importNbtStructures(structureRoot);
            convertAndUpdateSnbtStructures(output, structureRoot, outputStructureRoot);
        }

        return CompletableFuture.completedFuture(null);
    }

    private static void importNbtStructures(Path inputRoot) {
        try {
            Files.walkFileTree(inputRoot, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    String fileName = path.getFileName().toString();
                    if (!fileName.endsWith(".nbt")) {
                        return FileVisitResult.CONTINUE;
                    }
                    Path snbtPath = path.resolveSibling(replaceExtension(fileName, ".nbt", ".snbt"));
                    CompoundTag tag = NbtIo.readCompressed(path, NbtAccounter.unlimitedHeap());
                    Files.writeString(snbtPath, NbtUtils.structureToSnbt(tag), StandardCharsets.UTF_8);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to import structure files", e);
        }
    }

    private static void convertAndUpdateSnbtStructures(CachedOutput output, Path inputRoot, Path outputRoot) {
        try {
            Files.walkFileTree(inputRoot, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    String fileName = path.getFileName().toString();
                    if (!fileName.endsWith(".snbt")) {
                        return FileVisitResult.CONTINUE;
                    }
                    CompoundTag tag;
                    try {
                        tag = NbtUtils.snbtToStructure(Files.readString(path, StandardCharsets.UTF_8));
                    } catch (CommandSyntaxException e) {
                        LOGGER.error("Failed to parse SNBT for {}", path, e);
                        return FileVisitResult.CONTINUE;
                    }
                    String convertedFileName = replaceExtension(fileName, ".snbt", ".nbt");
                    Path convertedPath = outputRoot.resolve(inputRoot.relativize(path.resolveSibling(convertedFileName)));
                    writePackedStructure(output, convertedPath, tag);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to convert structure files", e);
        }
    }

    private static void writePackedStructure(CachedOutput output, Path path, CompoundTag tag) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        HashingOutputStream hashingOutput = new HashingOutputStream(Hashing.sha1(), bytes);
        NbtIo.writeCompressed(tag, hashingOutput);
        output.writeIfNeeded(path, bytes.toByteArray(), hashingOutput.hash());
    }

    private static String replaceExtension(String path, String oldSuffix, String newSuffix) {
        if (!path.endsWith(oldSuffix)) {
            throw new IllegalArgumentException("Cannot replace extension of " + path + " with " + newSuffix + ", as it does not have " + oldSuffix);
        }
        return path.substring(0, path.length() - oldSuffix.length()) + newSuffix;
    }

    @Override
    public String getName() {
        return "Structure Converter (" + namespace + ")";
    }
}
