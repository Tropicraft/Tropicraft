function initializeCoreMod() {
    return {
        'armor-rendertype': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.ArmorLayer',
                'methodName': 'renderArmor',
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V'
            },
            'transformer': function (method) {
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                
                var firstAload = ASM.findFirstInstruction(method, Opcodes.ALOAD);
                
                // Add model to the bottom of the stack for instance method call
                method.instructions.insert(firstAload, new VarInsnNode(Opcodes.ALOAD, 5));
                
                // Map method names
                var getEntityCutoutNoCull = ASM.mapMethod("func_228640_c_");
                var getRenderType = ASM.mapMethod("func_228282_a_");
                
                // Find and replace static method call with call to model.getRenderType
                var methodCall = ASM.findFirstMethodCall(method, ASM.MethodType.STATIC, 'net/minecraft/client/renderer/RenderType', getEntityCutoutNoCull, '(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;');
                if (!methodCall) {
                    throw 'Could not find method call';
                }
                method.instructions.set(methodCall, ASM.buildMethodCall('net/minecraft/client/renderer/entity/model/BipedModel', getRenderType, '(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;', ASM.MethodType.VIRTUAL));
                return method;
            }
        }
    }
}