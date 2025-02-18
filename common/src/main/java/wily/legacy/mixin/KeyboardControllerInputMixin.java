package wily.legacy.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wily.legacy.Legacy4JClient;
import wily.legacy.client.controller.ComponentState;
import wily.legacy.client.controller.ControllerComponent;

@Mixin(KeyboardInput.class)
public class KeyboardControllerInputMixin extends Input {
    @Redirect(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/player/KeyboardInput;forwardImpulse:F", ordinal = 0))
    private void calculateImpulse(KeyboardInput instance, float value) {
        ComponentState.Axis leftStick = (ComponentState.Axis) Legacy4JClient.controllerHandler.getButtonState(ControllerComponent.LEFT_STICK);
        forwardImpulse = leftStick.pressed && (up || down) ? -leftStick.getSmoothY() : value;
    }
    @Redirect(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/player/KeyboardInput;leftImpulse:F", ordinal = 0))
    private void calculateImpulseLeft(KeyboardInput instance, float value) {
        ComponentState.Axis leftStick = (ComponentState.Axis) Legacy4JClient.controllerHandler.getButtonState(ControllerComponent.LEFT_STICK);
        leftImpulse = leftStick.pressed && (left || right)  ? -leftStick.getSmoothX() : value;
    }
}
