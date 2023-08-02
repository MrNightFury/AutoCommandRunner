package ru.mrnightfury.autologger.mixins;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.mrnightfury.autologger.CommandsManager;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {
	@Shadow @Final private ServerInfo server;

	@Accessor
	abstract ServerInfo getServer();

	private TextFieldWidget commandField;
	private String command;
	protected AddServerScreenMixin(Text title) {
		super(title);
	}

//	@ModifyArg(method = "init()V", at = @At(value = "INVOKE", target = ""))

	@ModifyConstant(method = "init()V", constant = @Constant(intValue = 72))
	public int foo1(int x) {
		return 110;
	}
	@ModifyConstant(method = "init()V", constant = @Constant(intValue = 96))
	public int foo2(int x) {
		return 135 - 18;
	}
	@ModifyConstant(method = "init()V", constant = @Constant(intValue = 120))
	public int foo3(int x) {
		return 160 - 18;
	}

	@Inject(at = @At("HEAD"), method = "init()V")
	private void init (CallbackInfo info) {
//		this.commandField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 146, 200, 20, Text.translatable("addServer.enterIp"));
		this.commandField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 146, 200, 20, Text.translatable("autologger:addServerScreen.enterCommand"));
		this.commandField.setMaxLength(128);
		String command = CommandsManager.getCommand(this.server.address);
		this.commandField.setText(command == null ? "" : command);
		this.addSelectableChild(this.commandField);
		this.addDrawableChild(this.commandField);


//		this.commandField.setText(this.server.address);
//		this.commandField.setChangedListener(command -> this.updateAddButton());
	}

	@Inject(at = @At("HEAD"), method = "addAndClose()V")
	private void addAndClose(CallbackInfo info) {
		CommandsManager.addCommand(getServer().address, this.commandField.getText());
	}

	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		context.drawTextWithShadow(this.textRenderer, Text.translatable("autologger:addServerScreen.enterCommand"), this.width / 2 - 100, 134, 0xA0A0A0);
	}
}
