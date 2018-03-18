package net.tropicraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.sound.TropicraftSounds;
import net.tropicraft.core.encyclopedia.Encyclopedia;
import net.tropicraft.core.encyclopedia.TropicalBook;
import net.tropicraft.core.encyclopedia.TropicalBook.ContentMode;
import net.tropicraft.core.registry.SoundRegistry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class GuiTropicalBook extends GuiScreen {

	private TropicalBook book;
	private int indexPage = -1;
	private int selectedIndex = -1;
	private int contentPage = 0;
	private String coverBackground;
	private String pageBackgroundL, pageBackgroundR;
	private String closedTextureIndex;
	private String openTextureIndex;
	private RenderItem itemRenderer;
	private @Nonnull List<IRecipe> cachedRecipes = new ArrayList<>();
	private float recipeCycle;
	
	private GuiIndexButton[][] indexButtons;
	
	private GuiButton prevPage, nextPage;
	private GuiButton prevContentPage, nextContentPage;
	
	private final static int buttonNextIndexPage = 2000;
	private final static int buttonPrevIndexPage = 2001;
	private final static int buttonBookCover = 2003;
	private final static int buttonCraftingPage = 2010;
	private final static int buttonInfoPage = 2011;
	private final static int buttonNextContentPage = 2012;
	private final static int buttonPrevContentPage = 2013;

	public GuiTropicalBook(TropicalBook tropbook) {
		book = tropbook;
		indexButtons = new GuiIndexButton[2][book.entriesPerIndexPage()];
		coverBackground = tropbook.outsideTexture;
		pageBackgroundL = "encyclopedia_background_left";
	    pageBackgroundR = "encyclopedia_background_right";
		closedTextureIndex = tropbook.outsideTexture;
		openTextureIndex = tropbook.insideTexture;
		itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui(){
		addButtons();
		updateIndex();
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton guibutton) {

		switch (guibutton.id) {
		case buttonBookCover:
			indexPage = 0;
			contentPage = 0;
			addButtons();
			updateIndex();
			break;
		case buttonNextIndexPage:
			indexPage++;
			contentPage = 0;
			addButtons();
			updateIndex();
			break;
		case buttonPrevIndexPage:
			indexPage--;
			contentPage = 0;
			addButtons();
			updateIndex();
			break;
		case buttonNextContentPage:
			contentPage++;
			addButtons();
			break;
		case buttonPrevContentPage:
			contentPage--;
			addButtons();
			break;
		default:
			// Selected a page from the index list
			selectedIndex = (indexPage * book.entriesPerIndexPage() * 2) + guibutton.id;
			if (book.isPageVisible(selectedIndex) && !book.hasPageBeenRead(selectedIndex)) {
				book.markPageAsRead(selectedIndex);
			}
			contentPage = 0;
			cachedRecipes = ((Encyclopedia)book).getRecipesForEntry(selectedIndex);
			recipeCycle = 0;
			addButtons();
			updateIndex();
		}
	}


	private void addButtons() {

		buttonList.clear();

		if (indexPage == -1) {
			buttonList.add(new GuiClearButton(buttonBookCover, 0, 0, width, height, "", 0, coverBackground, 0x440000));
		} else if (selectedIndex >= 0) {
            // Add prev/next page for content //
            buttonList.add(prevContentPage = new GuiClearButton(buttonPrevContentPage, width / 2 - 164, height / 2 - 20 , 11, 22, "", 1, openTextureIndex, 0x440000));
            prevContentPage.visible = contentPage > 0;

            buttonList.add(nextPage = new GuiClearButton(buttonNextContentPage, width / 2 + 152, height / 2 - 20, 11, 22, "", 2, openTextureIndex, 0x440000));
            int max = contentPage == 0 ? 0 : (contentPage * 6) + 3;
            nextPage.visible = cachedRecipes.size() > max;
		} else {

			// Add index buttons //
			int perPage = book.entriesPerIndexPage();
			for (int i = 0; i < perPage * 2; i++) {
			    int row = i % perPage;
			    int col = i / perPage;
				buttonList.add(indexButtons[col][row] = new GuiIndexButton(i, width / 2 + (col == 0 ? -129 : 35), height / 2 - 87 + row * 15, 116, 10, "", -1, openTextureIndex, -1));
			}

			// Add prev/next page for index //
			buttonList.add(prevPage = new GuiClearButton(buttonPrevIndexPage , width / 2 - 164, height / 2 - 20 , 11, 22, "", 1, openTextureIndex, 0x440000));
			prevPage.visible = indexPage > 0;
			
			buttonList.add(nextPage = new GuiClearButton(buttonNextIndexPage, width / 2 + 152, height / 2 - 20, 11, 22, "", 2, openTextureIndex, 0x440000));
			nextPage.visible = (indexPage + 1) * (book.entriesPerIndexPage() * 2) < book.getPageCount();
        }
    }

    private void updateIndex() {
        if (indexPage < 0) {
            return;
        }
        for (int col = 0; col < indexButtons.length; col++) {
            for (int row = 0; row < indexButtons[col].length; row++) {
                GuiIndexButton button = indexButtons[col][row];
                int entry = indexPage * (book.entriesPerIndexPage() * 2) + row + (col * book.entriesPerIndexPage());
                if (entry < book.getPageCount()) {
                    String pageTitle = book.getPageTitleNotVisible(entry);
                    int color = 0x440000;
                    if (book.isPageVisible(entry)) {
                        pageTitle = book.getPageTitleByIndex(entry);

                        if (!book.hasPageBeenRead(entry)) {
                            color = 0x3333ff;
                        }
                    }
                    
                    // Make sure the title fits
                    int titleW = fontRenderer.getStringWidth(pageTitle);
                    int maxW = 116;
                    if (titleW > maxW) {
                        pageTitle += "...";
                    }
                    while (titleW > maxW) {
                        pageTitle = pageTitle.substring(0, pageTitle.length() - 4) + "...";
                        titleW = fontRenderer.getStringWidth(pageTitle);
                    }
                    
                    button.displayString = pageTitle;
                    button.color = color;
                    button.visible = true;
                    button.pageID = entry;
                } else {
                    button.visible = false;
                }
            }
        }
    }

	public void addIcons() {
        for (int col = 0; col < indexButtons.length; col++) {
            for (int row = 0; row < indexButtons[col].length; row++) {
                GuiIndexButton button = indexButtons[col][row];
                if (button.visible && button.pageID < book.getPageCount()) {
        			GlStateManager.pushMatrix();
        			GlStateManager.disableLighting();
        
        			TropicraftRenderUtils.bindTextureGui(openTextureIndex);
        			//mc.renderEngine.bindTextureMod(openTextureIndex);
        			GlStateManager.scale(.75F, .75F, .75F);
        			GlStateManager.translate((float) width / 1.5F, (float) height / 1.5F, 0f);
        			drawTexturedModalRect((int) ((col == 0 ? -130 : 16) * 1.5), -(int) (81 * 1.5) + row * 20, 3, 190, 18, 18);
        			GlStateManager.popMatrix();
        
        			//RenderHelper.disableStandardItemLighting();
        			GlStateManager.pushMatrix();
        			GlStateManager.enableLighting();
        			RenderHelper.enableGUIStandardItemLighting();
        			GlStateManager.scale(.75F, .75F, .75F);
        			GlStateManager.translate((float) width / 1.5F - 1F, (float) height / 1.5F, 0f);
        			GlStateManager.color(0, 0, 0);
        			// TODO 1.12 ??
        //			itemRenderer.isNotRenderingEffectsInGUI(book.isPageVisible(entry));
        			ItemStack is = book.getPageItemStack(button.pageID);
        			if(is != null) {
        				itemRenderer.renderItemIntoGUI(is, (int) ((col == 0 ? -129 : 17.5) * 1.5), -(int) (80 * 1.5) + row * 20);
        			}
        			GlStateManager.popMatrix();
                }
            }
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mousebutton) {
		if (mousebutton == 0) {
			for (int l = 0; l < buttonList.size(); l++) {
				GuiButton guibutton = (GuiButton) buttonList.get(l);
				if (guibutton.mousePressed(mc, x, y)) {
					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
							TropicraftSounds.PAGE_FLIP, 1.0F));
					actionPerformed(guibutton);
				}
			}
		} else if (mousebutton == 1) {
		    if (selectedIndex >= 0) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(TropicraftSounds.PAGE_FLIP, 1.0F));
                selectedIndex = -1;
                addButtons();
                updateIndex();
		    } else if (indexPage >= 0) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(TropicraftSounds.PAGE_FLIP, 1.0F));
                indexPage = -1;
                addButtons();
		    }
		}
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();
		if (Keyboard.getEventKeyState()) {
			if (Keyboard.getEventKey() == mc.gameSettings.keyBindInventory.getKeyCode()) {
				mc.displayGuiScreen(new GuiInventory(mc.player));
				return;
			}
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
	}

	private List<String> ttLines;

	@Override
	public void drawScreen(int i, int j, float elapsedPartialTicks) {

	    ttLines = null;
	    
		drawDefaultBackground();
		
		int w = width / 2;
		int h = height / 2;

		if (indexPage == -1) {
			// Draw outer book cover //
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			TropicraftRenderUtils.bindTextureGui(coverBackground);
			drawTexturedModalRect(w - 64, h - 86, 0, 0, 128, 173);
		} else {
			// Draw table of contents //
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			
			// Left page
			TropicraftRenderUtils.bindTextureGui(pageBackgroundL);
			drawTexturedModalRect(w - 167, h - 117, 89, 0, 167, 235);

			// Right page
			TropicraftRenderUtils.bindTextureGui(pageBackgroundR);
            drawTexturedModalRect(w, h - 117, 0, 0, 166, 235);
            
            if (selectedIndex >= 0) {
                // Draw content for selected index page //
                if (book.isPageVisible(selectedIndex)) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableLighting();
                    String pageTitle = book.isPageVisible(selectedIndex) ? book.getPageTitleByIndex(selectedIndex) : "\247nPage not found";
                    
                    // Render item title and description
                    if (contentPage == 0) {
                        fontRenderer.drawString(pageTitle, width / 2 - 150, height / 2 - 110, 0x440000);
                        fontRenderer.drawSplitString("  " + (book.isPageVisible(selectedIndex) ? book.getPageDescriptionsByIndex(selectedIndex) : "???"), width / 2 - 150, height / 2 - 80, 135, 0x440000);
                    }
                    if (cachedRecipes.size() > 0) {
                        fontRenderer.drawString("Crafting", width / 2 + 110, height / 2 - 110, 0x440000);
                        if (contentPage > 0) {
                            fontRenderer.drawString("Crafting", width / 2 - 150, height / 2 - 110, 0x440000);
                        }
                        GlStateManager.color(1, 1, 1);
                        TropicraftRenderUtils.bindTextureGui(openTextureIndex);
                        drawTexturedModalRect(width / 2 + 45, height / 2 - 115, 122, 201, 113, 32);
                        if (contentPage > 0) {
                            drawTexturedModalRect(width / 2 - 159, height / 2 - 115, 145, 201, 113, 32);
                        }
                    }
                    
                    // Draw recipe(s)
                    try {
                        printRecipes(i, j, elapsedPartialTicks);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (contentPage == 0) {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        GlStateManager.disableLighting();
                        TropicraftRenderUtils.bindTextureGui(openTextureIndex);

                        // Draw title underline and item box
                        drawTexturedModalRect(width / 2 - 159, height / 2 - 115, 145, 201, 113, 32);
                        drawTexturedModalRect(width / 2 - 47, height / 2 - 115, 90, 201, 32, 32);

                        // Render item icon
                        if (book.hasIndexIcons()) {
                            GlStateManager.pushMatrix();
                            GlStateManager.color(0, 0, 0);
                            // itemRenderer.renderWithColor = book.isPageVisible(selectedIndex);
                            // TODO 1.12 ??
                            // itemRenderer.isNotRenderingEffectsInGUI(book.isPageVisible(selectedIndex));
                            ItemStack is = book.getPageItemStack(selectedIndex);
                            if (is != null) {
                                GlStateManager.enableRescaleNormal();
                                RenderHelper.enableGUIStandardItemLighting();
                                GlStateManager.scale(1.25f, 1.25f, 1.25f);
                                itemRenderer.renderItemIntoGUI(is, (int) ((width / 2) * (4 / 5f) - 33), (int) ((height / 2) * (4 / 5f) - 87));
                                RenderHelper.disableStandardItemLighting();
                                GlStateManager.disableRescaleNormal();
                            }
                            GlStateManager.popMatrix();
                        }
                    }
                }
            } else {
                
                // TOC underline
                TropicraftRenderUtils.bindTextureGui(openTextureIndex);
                drawTexturedModalRect(w - 156, h - 102, 122, 214, 134, 8);
                
                fontRenderer.drawString("Table of Contents", w - 150, h - 110, 0x440000);
                fontRenderer.drawString("" + (1 + (indexPage * 2)), w - 157, h + 97, 0x440000);
                String secondPageNum = "" + (2 + (indexPage * 2));
                int sw = fontRenderer.getStringWidth(secondPageNum);
                fontRenderer.drawString(secondPageNum, w + 157 - sw, h + 97, 0x440000);
                if (book.hasIndexIcons()) {
                    addIcons();
    			}
            }
		}

		super.drawScreen(i, j, elapsedPartialTicks);
		
		if (ttLines != null) {
		    drawHoveringText(ttLines, i, j);
		}
	}

	private void printRecipes(int mx, int my, float elapsedPartialTicks) throws Exception {
	    recipeCycle += elapsedPartialTicks;
		if (cachedRecipes.isEmpty()) {
			return;
		}
		int newx = contentPage == 0 ? width / 2 + 25 : width / 2 - 136;
		int newy = height / 2 - 80;

		ContentMode contentMode = ContentMode.RECIPE;
		int max = book.entriesPerContentPage(contentMode);
		if (contentPage > 0) {
		    max *= 2;
		}
		int start = contentPage == 0 ? 0 : ((contentPage - 1) * 6) + 3;
		max += start;
		int idx = 0;
		for (int entry = start; entry < max; entry++) {
			if (entry >= cachedRecipes.size()) {
				break;
			}
			Encyclopedia.RecipeEntry recipe = ((Encyclopedia)book).getFormattedRecipe(cachedRecipes.get(entry));
			if (recipe == null) {
			    continue; // FIXME
			}
			
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1);

			// Draw recipe frame //
			TropicraftRenderUtils.bindTextureGui(openTextureIndex);
			drawTexturedModalRect(newx - 3, newy - 3, 0, 187, 122, 60);

			int offsetX = 18;
			int offsetY = 18;

			// Draw recipe ingredients //
			for(int row = 0; row < recipe.height; row++) {
				for (int col = 0; col < recipe.width; col++) {
					int itemIndex = (row * recipe.width) + col;
					if (recipe.ingredients.get(itemIndex) != Ingredient.EMPTY) {
						int renderX = newx + (offsetX * col) + 1;
						int renderY = newy + (offsetY * row) + 1;
						//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GlStateManager.pushMatrix();
						GlStateManager.enableRescaleNormal();
						RenderHelper.enableGUIStandardItemLighting();
						// TODO 1.12 seriously what was this method for
//						itemRenderer.isNotRenderingEffectsInGUI(true);
						//itemRenderer.renderWithColor = true;
						itemRenderer.renderItemIntoGUI(recipe.getCycledStack(itemIndex, recipeCycle), renderX, renderY);
						//itemRenderer.renderWithColor = false;
//						itemRenderer.isNotRenderingEffectsInGUI(false);
						RenderHelper.disableStandardItemLighting();
						GlStateManager.disableRescaleNormal();
						GlStateManager.popMatrix();
					}
				}
			}

			// Draw item label if mouse is hovering over an item
			for (int row = 0; row < recipe.height; row++) {
				for (int col = 0; col < recipe.width; col++) {
					int itemIndex = (row * recipe.width) + col;
					int renderX = newx + (offsetX * col) + 1;
					int renderY = newy + (offsetY * row) + 1;
					checkMouseHover(recipe.getCycledStack(itemIndex, recipeCycle), renderX, renderY, mx, my, 18);
				}
			}

			// Draw recipe output ItemStack //
			GlStateManager.pushMatrix();
//			GlStateManager.scale(1.25F, 1.25F, 1.25F);
//			GlStateManager.translate(newx / 1.93F + 1F, newy / 1.85F - .75F, 0F);
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();
			itemRenderer.renderItemIntoGUI(recipe.output, newx + 95, newy + 19);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, recipe.output, newx / 3 + 60, newy / 3 + 11, "");
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			checkMouseHover(recipe.output, newx + 90, newy + 20, mx, my, 25);
			GlStateManager.popMatrix();

			newy += 62;
			idx++;
			if (idx == 3) {
			    newx = width / 2 + 25;
			    newy = height / 2 - 80;
			}
		}
	}
	
	private void checkMouseHover(Ingredient ingredient, int x, int y, int mx, int my, int size) {
	    if (ingredient.getMatchingStacks().length > 0) {
	        checkMouseHover(ingredient.getMatchingStacks()[0], x, y, mx, my, size);
	    }
	}

	/**
	 * Draw the name of an item when it's hovered over in game
	 * @param itemstack ItemStack being hovered over
	 * @param x x coord
	 * @param y y coord
	 * @param size size...of something idk what :D
	 */
	private void checkMouseHover(ItemStack itemstack, int x, int y, int mx, int my, int size) {		
		boolean checkHover = (mx >= x && my >= y && mx < x + size && my < y + size);
		float z = this.zLevel;
		this.zLevel = 500;
		if (!itemstack.isEmpty() && checkHover) {
		    ITooltipFlag flag = mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;
		    ItemTooltipEvent evt = new ItemTooltipEvent(itemstack, mc.player, itemstack.getTooltip(mc.player, flag), flag);
		    if (!MinecraftForge.EVENT_BUS.post(evt)) {
		        ttLines = evt.getToolTip();
		    } else {
		        ttLines = null;
		    }
		}
		this.zLevel = z;
	}
}