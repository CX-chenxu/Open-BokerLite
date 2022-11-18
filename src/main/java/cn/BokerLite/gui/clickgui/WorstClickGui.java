/*
 * Decompiled with CFR 0.136.
 */
package cn.BokerLite.gui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import cn.BokerLite.Client;
import cn.BokerLite.command.Command;
import cn.BokerLite.command.CommandManager;
import cn.BokerLite.gui.FontRenderer;
import cn.BokerLite.gui.clickgui.Opacity;
import cn.BokerLite.gui.clickgui.components.Component;
import cn.BokerLite.gui.clickgui.components.ModuleButton;
import cn.BokerLite.gui.clickgui.components.ModuleWindow;
import cn.BokerLite.gui.clickgui.components.SubWindow;
import cn.BokerLite.gui.configgui.implement.GuiInputBox;
import cn.BokerLite.modules.Module;
import cn.BokerLite.modules.ModuleManager;
import cn.BokerLite.modules.ModuleType;
import cn.BokerLite.modules.value.Mode;
import cn.BokerLite.modules.value.Numbers;
import cn.BokerLite.modules.value.Option;
import cn.BokerLite.modules.value.Value;

import cn.BokerLite.utils.fontRenderer.FontLoaders;
import cn.BokerLite.utils.mod.Helper;
import cn.BokerLite.utils.render.ColorUtils;
import cn.BokerLite.utils.render.RenderUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;



import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;

import static cn.BokerLite.modules.ModuleManager.readSettings;


public class WorstClickGui extends GuiScreen implements GuiYesNoCallback {
	public static ModuleType currentModuleType = ModuleType.Combat;
	public static Module currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
			? ModuleManager.getModulesInType(currentModuleType).get(0)
			: null;
	public static float startX = 100, startY = 100;
	private static final ArrayList<SubWindow> components = new ArrayList<>();
	private static GuiInputBox searchBox;
	private static GuiInputBox chatBox;
	public float moduleStart = 0;
	public int valueStart = 0;
	private static final boolean chat = true;
	boolean previousmouse = true;
	boolean mouse;
	public Opacity opacity = new Opacity(0);
	public int opacityx = 255;
	float animationDWheel;

	int finheight;
	float animheight = 0;
	public float moveX = 0, moveY = 0;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {


		chatBox.drawTextBox();
		for (SubWindow component : components) {
			GlStateManager.pushMatrix();
			component.drawComponent(mouseX - component.getX(), mouseY - component.getY(), partialTicks);
			GlStateManager.popMatrix();
		}
		RenderUtil.drawRect(0, 0, width, height, new Color(0, 0, 0, 140).getRGB());
		if (isHovered(startX, startY - 25, startX + 400, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
		this.opacity.interpolate((float) opacityx);
		Color baseColor = new Color(3,11,23, 110);
		Color colorr = ColorUtils.interpolateColorC(baseColor, new Color(ColorUtils.applyOpacity(baseColor.getRGB(), .3f)), 0.5F);
		//  RenderUtil.drawRect(this.startX+10, this.startY, this.startX + 340, this.startY +2, new Color(255, 255, 255).getRGB());

		RenderUtil.drawRect(startX +10, startY-30 , startX + 85 , startY + 280 ,colorr.getRGB());
		RenderUtil.drawRect(startX +85, startY-30 , startX + 360 , startY + 280 ,(new Color(9,8,14)).getRGB());
		RenderUtil.drawRect(startX +85, startY -5, startX + 360 , startY -6 ,(new Color(14,23,30)).getRGB());
		RenderUtil.drawRect(startX +86, (float) (startY +0.0), startX + 180 , startY + 270 ,(new Color(1,11,21)).getRGB());
		RenderUtil.drawRect(startX +182, (float) (startY+0.0), startX + 355 , startY + 270 ,(new Color(1,11,21)).getRGB());
		RenderUtil.drawRect(startX +88, (float) (startY +17.0), startX + 178 , startY + 19 ,(new Color(5,23,37)).getRGB());
		RenderUtil.drawRect(startX +184, (float) (startY+17.0), startX + 353 , startY + 19 ,(new Color(5,23,37)).getRGB());
		RenderUtil.drawRect(startX  +13, startY + 106, startX + 82, startY +105,(new Color(5,166,238)).getRGB());
		//   RenderUtil.drawRect(this.startX+10, this.startY+2, this.startX + 340, this.startY + 230, new Color(27, 27, 27).getRGB());

		//	RenderUtil.drawGradientSideways(this.startX+13,  this.startY +3,  this.startX + 337 + 0.5D,this.startY + 3 + 0.3D,-1, Colors.getColor(0, 200));

		FontRenderer.F28.drawString("BokerLite ", startX + 17, startY + 2, new Color(254, 254, 254, (int) opacity.getOpacity()).getRGB());

		for (int i = 0; i < ModuleType.values().length; i++) {
			ModuleType[] iterator = ModuleType.values();
			if (iterator[i] == currentModuleType) {
				finheight = i * 15;
				//Gui.drawGradientRect2(startX-40,startY+50+animheight,startX+60,startY+75+animheight,new Color(0,80,255).getRGB(),new Color(0,150,255).getRGB());

				RenderUtil.drawRoundedRect(startX +12, startY + 26 + animheight, startX +83, startY + 40 + animheight, 1,(new Color(4,51,77)).getRGB());

				animheight = (float) RenderUtil.getAnimationState(animheight, finheight, Math.max(100f, Math.abs(finheight - animheight) * 10f));

					/*if(animheight<finheight){
						if(finheight - animheight<30) {
							animheight+=2;
						}else{
							animheight+=2;
						}
					}else if(animheight>finheight){
						if(animheight - finheight<30) {
							animheight-=2;
						}else{
							animheight-=2;
						}
					}*/
				if (animheight == finheight) {
					FontRenderer.F20.drawString(iterator[i].name(), startX +25, startY + 27 + i * 15, new Color(255, 255, 255).getRGB());
				} else {
//						Gui.drawRect(startX-25,startY+50+i*30,startX+60,startY+45+i*30,new Color(255,255,255,0).getRGB());
					FontRenderer.F20.drawString(iterator[i].name(), startX  +25, startY + 27 + i * 15,  new Color(255, 255, 255).getRGB());
				}
			} else {
				RenderUtil.drawRect(startX - 45, startY + 50 + i * 30, startX + 60, startY + 75 + i * 15, new Color(255, 255, 255, 0).getRGB());
				FontRenderer.F20.drawString(iterator[i].name(), startX  +25, startY + 27 + i * 15,  new Color(255, 255, 255).getRGB());
			}
			try {
				if (this.isCategoryHovered(startX  +25, startY + 20 + i * 15, startX + 60, startY + 45 + i * 25, mouseX,
						mouseY) && Mouse.isButtonDown(0) ) {
					currentModuleType = iterator[i];
					currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
							? ModuleManager.getModulesInType(currentModuleType).get(0)
							: null;
					moduleStart = 0;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
        int a = 255;
		int b = 255;
		if (this.isCategoryHovered(startX  +12, startY + 114, startX + 83, startY +128, mouseX,
				mouseY) ) {
			 a = 0;
			RenderUtil.drawRoundedRect(startX  +12, startY + 114, startX + 83, startY +128,1, (new Color(14,23,30)).getRGB());

		}
		if (this.isCategoryHovered(startX  +12, startY + 133, startX + 83, startY +148, mouseX,
				mouseY) ) {
			b = 0;
			RenderUtil.drawRoundedRect(startX  +12, startY + 133, startX + 83, startY +148,1, (new Color(14,23,30)).getRGB());

		}
		if (this.isCategoryHovered(startX  +12, startY + 114, startX + 83, startY +128, mouseX,
				mouseY)  && Mouse.isButtonDown(0)) {
			Client.INSTANCE.shutDown();
		//	a = 0;
			//RenderUtil.drawRoundedRect(startX  +12, startY + 114, startX + 83, startY +128,1, (new Color(14,23,30)).getRGB());

		}
		if (this.isCategoryHovered(startX  +12, startY + 133, startX + 83, startY +148, mouseX,
				mouseY) && Mouse.isButtonDown(0) ) {
			readSettings();
			//b = 0;
		//	RenderUtil.drawRoundedRect(startX  +12, startY + 133, startX + 83, startY +148,1, (new Color(14,23,30)).getRGB());

		}
		RenderUtil.drawRoundedRect(startX  +12, startY + 114, startX + 83, startY +128,1, (new Color(4,51,77,a)).getRGB());
		RenderUtil.drawRoundedRect(startX  +12, startY + 133, startX + 83, startY +148,1, (new Color(4,51,77,b)).getRGB());
		FontRenderer.F20.drawString("Load", startX  +31, startY + 135,new Color(255, 255, 255).getRGB());
		FontRenderer.F20.drawString("Save", startX  +31, startY + 115,new Color(255, 255, 255).getRGB());
		int m = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 60, startY, startX + 200, startY + 235, mouseX, mouseY)) {
			if (m < 0 && moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
				moduleStart++;
				animationDWheel = (float) RenderUtil.getAnimationState(animationDWheel, 1, 50f);
				Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.2F, 2F);
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
				moduleStart = (float) RenderUtil.getAnimationState(moduleStart, -1, 50f);
				Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.2F, 2F);
			}
		} else {
			animationDWheel = 0.0f;
		}


		if (this.isCategoryHovered(startX + 200, startY, startX + 420, startY + 320, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}
		FontRenderer.F22.drawString(
				currentModuleType.toString(),
				startX + 90, startY + 4, -1);
		FontRenderer.F22.drawString(
				currentModule == null ? currentModuleType.toString()
						: currentModule.getName() ,
				startX + 190, startY + 4, -1);
		//	   Gui.drawRect(	startX + 279, startY + 5,	startX + 331, startY + 13, new Color(58,58,58).getRGB());


		if (currentModule != null) {
			float mY = startY + 16;
			for (int i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); i++) {
				Module module = ModuleManager.getModulesInType(currentModuleType).get(i);
				if (mY > startY + 250)
					break;
				if (i < moduleStart) {
					continue;
				}
				if (isSettingsButtonHovered(startX + 87, mY+4,
						startX + 125 + ( FontRenderer.F20.getStringWidth(module.getName())),
						mY + 24 , mouseX, mouseY)) {
					RenderUtil.drawRect(startX +87, mY+7 , startX + 176, mY + 26, new Color(255, 255, 255).getRGB());
				}
				RenderUtil.drawRect(startX +88, mY+7 , startX + 175, mY + 26, new Color(14,23,30).getRGB());
				//RenderUtil.drawRect(startX +88, mY+7 , startX +91, mY + 26, new Color(130,140,150).getRGB());
				FontRenderer.F20.drawString(module.getName(), startX + 105, mY + 10,
						new Color(130,140,150, (int) opacity.getOpacity()).getRGB());
				RenderUtil.circle(startX +95, mY+16 , 2, new Color(122,139,157).getRGB());
				if (module.getState()) {
					RenderUtil.drawRect(startX +88, mY+7 , startX + 175, mY + 26, new Color(4,51,77).getRGB());
					//	RenderUtil.drawRect(startX +88, mY+7 , startX +91, mY + 26, new Color(255,255,255).getRGB());
					FontRenderer.F20.drawString(module.getName(), startX + 105, mY + 10,
							new Color(255,255,255, (int) opacity.getOpacity()).getRGB());
					RenderUtil.circle(startX +95, mY+16 , 2, new Color(11,163,239).getRGB());
				}

				if(module.getValues().size()>0) {
					FontRenderer.F18.drawString("+", startX +167, mY+13 , new Color(150,150,150).getRGB());
				}
				if (isSettingsButtonHovered(startX + 87, mY+4,
						startX + 125 + ( FontRenderer.F20.getStringWidth(module.getName())),
						mY + 24 , mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown(0)) {
						module.setState(!module.getState());
						previousmouse = true;
					}
					if (!this.previousmouse && Mouse.isButtonDown(1)) {
						previousmouse = true;
					}
				}

				if (!Mouse.isButtonDown(0)) {
					this.previousmouse = false;
				}
				if (isSettingsButtonHovered(startX + 87, mY+4,
						startX + 125 + ( FontRenderer.F20.getStringWidth(module.getName())),
						mY + 24 , mouseX, mouseY) && Mouse.isButtonDown(1)) {
					currentModule = module;
					valueStart = 0;
				}
				mY += 20;
			}
			mY = startY + 25;
			for (int i = 0; i < currentModule.getValues().size(); i++) {
				if (mY > startY + 215)
					break;
				if (i < valueStart) {
					continue;
				}
				FontRenderer font =  FontRenderer.F18;
				Value value = currentModule.getValues().get(i);
				if (value instanceof Numbers) {
					float x = startX + 280;
					double render = 68.0F
							* (((Number) value.getValue()).floatValue() - ((Numbers) value).getMinimum().floatValue())
							/ (((Numbers) value).getMaximum().floatValue()
							- ((Numbers) value).getMinimum().floatValue());
					RenderUtil.drawRect(x - 6, mY + 2, (float) ((double) x + 74), mY + 3,
							(new Color(0,27,41, 255)).getRGB());
					RenderUtil.drawRect(x - 6, mY + 2, (float) ((double) x + render + 6.5D), mY + 3,
							(new Color(5,166,238, 255)).getRGB());
					RenderUtil.circle((float)(x + render + 4), mY +2, 2F, (new Color(5,166,238, 255)).getRGB());
					//RenderUtil.drawCircle((float) ((double) x + render + 2D), mY, 10,new Color(161, 141, 255).getRGB());
					//RenderUtil.drawRect((float) ((double) x + render + 2D), mY, (float) ((double) x + render + 7D),
					//	mY + 5, (new Color(61, 141, 255, (int) opacity.getOpacity())).getRGB());
					font.drawString(value.getName() + ": " + value.getValue(), startX + 190, mY, new Color(130,140,150).getRGB());
					if (!Mouse.isButtonDown(0)) {
						this.previousmouse = false;
					}
					if (this.isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY)
							&& Mouse.isButtonDown(0)) {
						if (!this.previousmouse && Mouse.isButtonDown(0)) {
							render = ((Numbers) value).getMinimum().doubleValue();
							double max = ((Numbers) value).getMaximum().doubleValue();
							double inc = ((Numbers) value).getIncrement().doubleValue();
							double valAbs = (double) mouseX - ((double) x + 1.0D);
							double perc = valAbs / 68.0D;
							perc = Math.min(Math.max(0.0D, perc), 1.0D);
							double valRel = (max - render) * perc;
							double val = render + valRel;
							val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
							value.setValue(Double.valueOf(val));
						}
						if (!Mouse.isButtonDown(0)) {
							this.previousmouse = false;
						}
					}
					mY += 20;
				}
				if (value instanceof Option) {
					float x = startX + 250;
					if (this.isCheckBoxHovered(x + 70, mY - valueStart+5, x + 78, mY - valueStart + 15, mouseX, mouseY)) {


					}
					FontRenderer.F18.drawString(value.getDisplayName(), startX + 190, mY, new Color(130,140,150, (int) opacity.getOpacity()).getRGB());
					Color buttonColor;
					if ((boolean) value.getValue()) {
						buttonColor = (new Color(5,166,238, 255));
					} else {
						buttonColor = new Color(130,140,150);
					}
					RenderUtil.circle(x + 75, mY - valueStart + 10, 4f, buttonColor.getRGB());
					if (this.isCheckBoxHovered(x + 70, mY - valueStart+5, x + 78, mY - valueStart + 15, mouseX, mouseY)) {
						//	RenderUtil.circle(x + 35, mY - valueStart + 2, 4f, new Color(-14848033).brighter());
						if (!this.previousmouse && Mouse.isButtonDown(0)) {
							this.previousmouse = true;
							this.mouse = true;
						}

						if (this.mouse) {
							value.setValue(!(boolean) value.getValue());
							this.mouse = false;
						}
					}
					if (!Mouse.isButtonDown(0)) {
						this.previousmouse = false;
					}
					mY += 20;
				}
				if (value instanceof Mode) {
					float x = startX + 245;
					font.drawString(value.getName(), startX + 190, mY, new Color(130,140,150, (int) opacity.getOpacity()).getRGB() );




					FontRenderer.F16.drawStringWithShadow(((Mode) value).getModeAsString(),
							(int) (x + 65 - font.getStringWidth(((Mode) value).getModeAsString()) / 2), (int) (mY+2),new Color(130,140,150, (int) opacity.getOpacity()).getRGB() );
					if (this.isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
						if (Mouse.isButtonDown(0) && !this.previousmouse) {
							Enum current = (Enum) value.getValue();
							int next = current.ordinal() + 1 >= ((Mode) value).getModes().length ? 0
									: current.ordinal() + 1;
							value.setValue(((Mode) value).getModes()[next]);
							this.previousmouse = true;
						}
						if (!Mouse.isButtonDown(0)) {
							this.previousmouse = false;
						}

					}
					mY += 25;
				}


			}

		}

	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for (int i = components.size() - 1; i >= 0; i--) {
			SubWindow window = components.get(i);
			if (mouseX > window.getX() && mouseX < window.getX() + window.getWidth() &&
					mouseY > window.getY() && mouseY < window.getY() + window.getHeight()) {
				window.mouseClicked(mouseX, mouseY, mouseButton);
				components.add(window);
				components.remove(i--);
				break;
			}
		}

		chatBox.mouseClicked(mouseX, mouseY, mouseButton);
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (Component component : components) {
			component.keyTyped(typedChar, keyCode);
		}
		if (chatBox != null) {
			chatBox.textboxKeyTyped(typedChar, keyCode);
		}
		assert chatBox != null;
		if (chatBox.isFocused()) {
			if (typedChar == '\r') {
				String msg = chatBox.getText();
				if (!Objects.equals(msg, "")) {
					if (msg.startsWith(".")) {
						if (msg.length() > 1) {
							String[] args = msg.trim().substring(1).split(" ");
							Optional<Command> possibleCmd = CommandManager.getCommand(args[0]);
							if (possibleCmd.isPresent()) {
								String result = possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
								if (result != null && !result.isEmpty()) {
									Helper.sendMessage(result);
								}
							} else {
								Helper.sendMessage("Command not found Try .help to find help.");
							}
						}
					} else {
						mc.thePlayer.sendChatMessage(chatBox.getText());
					}
				}
				chatBox.setText("");
			} else if (typedChar == '\t') {
				String msg = chatBox.getText();
				if (!Objects.equals(msg, "")) {
					if (msg.startsWith(".")) {
						String raw = msg.substring(1);
						if (!raw.contains(" ")) {
							StringBuilder ac = new StringBuilder();
							for (Command c : CommandManager.getCommands()) {
								ac.append(".").append(c.getName()).append(", ");
							}
							Helper.sendMessage(ac.toString());
						} else {
							String[] splited = raw.split(" ");
							for (Command c : CommandManager.getCommands()) {
								if (Objects.equals(c.getName(), splited[0])) {
									switch (CommandManager.getCommand(splited[0]).get().getAC()) {
										case Module: {
											StringBuilder ac = new StringBuilder();
											for (Module m : ModuleManager.getModules()) {
												ac.append(".").append(m.getName()).append(", ");
											}
											Helper.sendMessage(ac.toString());
										}

										case Player: {
											StringBuilder ac = new StringBuilder();
											for (NetworkPlayerInfo npi : mc.getNetHandler().getPlayerInfoMap()) {
												ac.append(".").append(npi.getGameProfile().getName()).append(", ");
											}
											Helper.sendMessage(ac.toString());
										}
										default:
											break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public void initGui() {
		if (Client.blur.getValue()) {
			mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}


		chatBox = new GuiInputBox(2, FontRenderer.F16, 0, height - 16, width, 16) {
		};
		chatBox.setPlaceholder("Chat or Command");
		chatBox.setMaxStringLength(256);
		chatBox.setCanLoseFocus(true);
		chatBox.setEnabled(true);


//        pw.init();
		super.initGui();
	}
	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
	}

	@Override
	public void onGuiClosed() {
		this.opacity.setOpacity(0);
	}
}
