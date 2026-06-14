import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingException;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.startup.StartupArgs;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ExplicitToImplicitClassMigration")
public class ToolRunner {
    @SuppressWarnings("UnnecessaryModifier")
    public static void main(String[] args) throws Throwable {
        bootstrapAndRun(args[0], "run", ArrayUtils.subarray(args, 1, args.length));
    }

    private static void bootstrapFml(String[] args) throws Throwable {
        StartupArgs startupArgs = new StartupArgs(
                Path.of(""),
                true,
                Dist.DEDICATED_SERVER,
                false,
                args,
                Set.of(),
                List.of(),
                Thread.currentThread().getContextClassLoader()
        );
        FMLLoader loader = FMLLoader.create(startupArgs);
        if (loader.getLoadingModList().hasErrors()) {
            throw new ModLoadingException(loader.getLoadingModList().getModLoadingIssues());
        }

        findAndInvoke("net.minecraft.SharedConstants", "tryDetectVersion");
        findAndInvoke("net.minecraft.server.Bootstrap", "bootStrap");
        findAndInvoke("net.neoforged.neoforge.server.loading.ServerModLoader", "load", MethodType.methodType(void.class, boolean.class), false);
    }

    private static void bootstrapAndRun(String className, String methodName, String[] args) throws Throwable {
        bootstrapFml(args);
        findAndInvoke(className, methodName);
    }

    private static void findAndInvoke(String className, String methodName) throws Throwable {
        findAndInvoke(className, methodName, MethodType.methodType(void.class));
    }

    private static void findAndInvoke(String className, String methodName, MethodType methodType, Object... args) throws Throwable {
        ClassLoader loader = FMLLoader.getCurrent().getCurrentClassLoader();
        Class<?> clazz = Class.forName(className, true, loader);
        MethodHandles.publicLookup().findStatic(clazz, methodName, methodType).invokeWithArguments(args);
    }
}
