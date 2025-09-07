package dev.miami.uwuifier.mixin;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Set;
import java.util.HashSet;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

	@Unique
	private boolean isThisActive = false;

	@Unique
	private static final Set<String> badwords = new HashSet<>() {{
		add("dox");
		add("swat");
		add("swatted");
		add("doxxed");
		add("kys");
		add("nigger");
		add("nigga");
		add("faggot");
		add("ddosed");
		add("ddos");
	}};

	@Invoker("sendChatMessage")
	public abstract void invokeSendChatMessage(String message);

	@Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void injectSendChatMessage(String message, CallbackInfo ci) {
		if (message.startsWith("/")) return;

		boolean containsbadwords = badwords.stream()
				.anyMatch(word -> message.toLowerCase().contains(word));

		if (!containsbadwords) return;

		if (isThisActive) return;
		try {
			isThisActive = true;
			invokeSendChatMessage("uwu");
		} finally {
			isThisActive = false;
		}

		ci.cancel();
	}
}
