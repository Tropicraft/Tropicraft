package net.tropicraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

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
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.sound.TropicraftSounds;
import net.tropicraft.core.encyclopedia.Page;
import net.tropicraft.core.encyclopedia.RecipeEntry;
import net.tropicraft.core.encyclopedia.TropicalBook;

@SideOnly(Side.CLIENT)
public class GuiTropicalBook extends GuiScreen {

	private TropicalBook book;
	private int indexPage = -1;
	private Page selectedPage;
	private int contentPage = 0;
	private String coverBackground;
	private String pageBackgroundL, pageBackgroundR;
	private String closedTextureIndex;
	private String openTextureIndex;
	private RenderItem itemRenderer;
	private @Nonnull List<RecipeEntry> cachedRecipes = new ArrayList<>();
	private float recipeCycle;
		
	private GuiButton coverButton;
	private GuiButton prevPage, nextPage;
	private GuiButton prevContentPage, nextContentPage;
	
	private List<List<GuiIndexButton>> pageButtons = new ArrayList<>();
	
	private final static int buttonNextIndexPage = -1;
	private final static int buttonPrevIndexPage = -2;
	private final static int buttonBookCover = -3;
	private final static int buttonCraftingPage = -4;
	private final static int buttonInfoPage = -5;
	private final static int buttonNextContentPage = -6;
	private final static int buttonPrevContentPage = -7;

	public GuiTropicalBook(TropicalBook tropbook) {
		book = tropbook;
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
		updateButtons();
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton guibutton) {

		switch (guibutton.id) {
		case buttonBookCover:
			indexPage = 0;
			contentPage = 0;
			updateButtons();
			break;
		case buttonNextIndexPage:
			indexPage++;
			contentPage = 0;
			updateButtons();
			break;
		case buttonPrevIndexPage:
			indexPage--;
			contentPage = 0;
			updateButtons();
			break;
		case buttonNextContentPage:
			contentPage++;
			updateButtons();
			break;
		case buttonPrevContentPage:
			contentPage--;
			updateButtons();
			break;
		default:
			// Selected a page from the index list
			selectedPage = ((GuiIndexButton)guibutton).getPage();
			if (book.isPageVisible(selectedPage.getId()) && !book.hasPageBeenRead(selectedPage.getId())) {
				book.markPageAsRead(selectedPage.getId());
			}
			contentPage = 0;
			cachedRecipes = selectedPage.getRelevantRecipes();
			recipeCycle = 0;
			updateButtons();
		}
	}
	
	private void updateButtons() {
	    coverButton.visible = indexPage == -1;
	    
        nextContentPage.visible = indexPage >= 0 && cachedRecipes.size() > (contentPage * 6) + 3;
        prevContentPage.visible = contentPage > 0;
        
        nextPage.visible = indexPage >= 0 && selectedPage == null && indexPage < pageButtons.size() - 1;
        prevPage.visible = indexPage > 0 && selectedPage == null;
        
        for (int i = 0; i < pageButtons.size(); i++) {
            for (GuiIndexButton btn : pageButtons.get(i)) {
                btn.visible = selectedPage == null && i == indexPage;
            }
        }
	}

	private void addButtons() {

		buttonList.clear();

		buttonList.add(coverButton = new GuiClearButton(buttonBookCover, 0, 0, width, height, "", 0, coverBackground, 0x440000));
		
        // Add prev/next page for content //
        buttonList.add(prevContentPage = new GuiClearButton(buttonPrevContentPage, width / 2 - 164, height / 2 - 20 , 11, 22, "", 1, openTextureIndex, 0x440000));
        buttonList.add(nextContentPage = new GuiClearButton(buttonNextContentPage, width / 2 + 152, height / 2 - 20, 11, 22, "", 2, openTextureIndex, 0x440000));

		// Add index buttons //
		boolean left = true;
		int y = 0;
		int page = 0;
		for (int entry = 0; entry < book.getPageCount(); entry++) {
		    if (book.isPageVisible(entry)) {
			    addIndexButton(entry, page, width / 2 + (left ? -150 : 14), height / 2 - 87 + y);
			    y += 20;
			    if (y > 175) {
			        if (!left) {
			            page++;
			        }
			        left = !left;
			        y = 0;
			    }
		    }
		}

		// Add prev/next page for index //
		buttonList.add(prevPage = new GuiClearButton(buttonPrevIndexPage , width / 2 - 164, height / 2 - 20 , 11, 22, "", 1, openTextureIndex, 0x440000));
		
		buttonList.add(nextPage = new GuiClearButton(buttonNextIndexPage, width / 2 + 152, height / 2 - 20, 11, 22, "", 2, openTextureIndex, 0x440000));
    }
	
	private void addIndexButton(int entry, int page, int x, int y) {
	    String pageTitle = book.getPageTitleNotVisible();
        int color = 0x440000;
        if (book.isPageVisible(entry)) {
            pageTitle = book.getPage(entry).getLocalizedTitle();

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
        
        GuiIndexButton btn = new GuiIndexButton(book.getPage(entry), entry, x, y, 116, 15, pageTitle, -1, openTextureIndex, color);
	    buttonList.add(btn);
	    if (pageButtons.size() == page) {
	        pageButtons.add(new ArrayList<>());
	    }
	    pageButtons.get(page).add(btn);
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
		    if (selectedPage != null) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(TropicraftSounds.PAGE_FLIP, 1.0F));
                selectedPage = null;
                updateButtons();
		    } else if (indexPage >= 0) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(TropicraftSounds.PAGE_FLIP, 1.0F));
                indexPage = -1;
                updateButtons();
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
            
            if (selectedPage != null) {
                // Draw content for selected index page //
                if (book.isPageVisible(selectedPage.getId())) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableLighting();
                    String pageTitle = book.isPageVisible(selectedPage.getId()) ? selectedPage.getLocalizedTitle() : book.getPageTitleNotVisible();
                    
                    // Render item title and description
                    if (contentPage == 0) {
                        boolean toolong = fontRenderer.getStringWidth(pageTitle) > 100 && !fontRenderer.getUnicodeFlag();
                        if (toolong) {
                            fontRenderer.setUnicodeFlag(true);
                        }
                        fontRenderer.drawString(pageTitle, width / 2 - 150, height / 2 - 110, 0x440000);
                        if (toolong) {
                            fontRenderer.setUnicodeFlag(false);
                        }
                        fontRenderer.drawSplitString("  " + (book.isPageVisible(selectedPage.getId()) ? selectedPage.getLocalizedDescription() : "???"), width / 2 - 150, height / 2 - 80, 135, 0x440000);
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
                        
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(1.25f, 1.25f, 1.25f);
                        selectedPage.drawIcon((int) ((width / 2) * (4 / 5f) - 33), (int) ((height / 2) * (4 / 5f) - 87), elapsedPartialTicks);
                        GlStateManager.popMatrix();
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

		int max = 3;
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
			RecipeEntry recipe = cachedRecipes.get(entry);
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
			for(int row = 0; row < recipe.getHeight(); row++) {
				for (int col = 0; col < recipe.getWidth(); col++) {
					int itemIndex = (row * recipe.getWidth()) + col;
					if (recipe.getIngredients().get(itemIndex) != Ingredient.EMPTY) {
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
			for (int row = 0; row < recipe.getHeight(); row++) {
				for (int col = 0; col < recipe.getWidth(); col++) {
					int itemIndex = (row * recipe.getWidth()) + col;
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
			itemRenderer.renderItemIntoGUI(recipe.getOutput(), newx + 95, newy + 19);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, recipe.getOutput(), newx / 3 + 60, newy / 3 + 11, "");
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			checkMouseHover(recipe.getOutput(), newx + 90, newy + 20, mx, my, 25);
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
		    ttLines = itemstack.getTooltip(mc.player, flag);
		}
		this.zLevel = z;
	}
}