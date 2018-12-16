package amata1219.hypering.economy.gui.home;

public class OldHomeMenu {

/*	private OldGUIManager manager;

	private OldHomeMenu(){

	}

	public static OldHomeMenu load(OldGUIManager manager){
		OldHomeMenu homeMenu = new OldHomeMenu();

		homeMenu.manager = manager;

		homeMenu.initialize();

		return homeMenu;
	}

	public void initialize(){
		Inventory inventory = manager.getInventory(Type.HOME_MENU);

		Player player = manager.getPlayer();
		UUID uuid = manager.getUniqueId();

		ItemStack status = ItemHelper.createSkull(player, ChatColor.GOLD + player.getName(), (String[]) null);

		if(HyperingEconomyGUI.isEnableEconomy()){
			ItemHelper.addLore(status, ChatColor.GRAY + "所持金ランキングの順位: " + HyperingEconomyGUI.getGUIListener().getPossesionMoneyRanking().getRank(uuid));
			ItemHelper.addLore(status, ChatColor.GRAY + "");

			HyperingEconomyAPI api =

			HyperingEconomyGUI.getHyperingEconomyAPI().getMoney(player, uuid, new Callback<Result>(){

				@Override
				public void done(Result result) {
					ItemHelper.addLore(status, ChatColor.GRAY + "所持金: ¥" + result.getLong());

					HyperingEconomyGUI.getHyperingEconomyAPI().getNumberOfTickets(player, uuid, new Callback<Result>(){

						@Override
						public void done(Result result) {
							ItemHelper.addLore(status, ChatColor.GRAY + "チケット: " + result.getLong() + "枚");
						}

					});
				}

			});
		}else{
			HyperingEconomyGUI.getHyperingEconomyAPI().getNumberOfTickets(player, uuid, new Callback<Result>(){

				@Override
				public void done(Result result) {
					ItemHelper.addLore(status, ChatColor.GRAY + "チケット: " + result.getLong() + "枚");
				}

			});
		}

		ItemStack send = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "お金を送る", ChatColor.GRAY + "・送金額を指定します。 ", ChatColor.GRAY + "・送金相手を指定します。");

		ItemStack buy = ItemHelper.createItem(Material.STORAGE_MINECART, ChatColor.GOLD + "チケットを購入する", ChatColor.GRAY + "・枚数を指定します。");

		ItemStack cash = ItemHelper.createItem(Material.MINECART, ChatColor.GOLD + "チケットを換金する", ChatColor.GRAY + "・枚数を指定します");

		ItemStack ranking = ItemHelper.createItem(Material.BLAZE_POWDER, ChatColor.GOLD + "所持金ランキング", ChatColor.GRAY + "");

		ItemStack auction = ItemHelper.createItem(Material.ANVIL, ChatColor.GOLD + "オークションメニュー", ChatColor.GRAY + "・オークションメニューに移動します。");

		ItemStack economy = ItemHelper.createItem(Material.RAW_FISH, ChatColor.GOLD + "ﾟ｡+━ヾ((○*>∀<*))ﾉﾞ━+｡ﾟ ＜チケットとお金が有効なのん！", ChatColor.GRAY + "");

		if(!HyperingEconomyGUI.getHyperingEconomyAPI().isEnableEconomy()){
			economy.setType(Material.COOKED_FISH);
			ItemHelper.changeDisplayName(economy, ChatColor.GOLD + "･ﾟ･(｡>ω<｡)･ﾟ･ ＜チケットは有効だけどお金は無効なのん…");
		}

		ItemStack vote = ItemHelper.createItem(Material.DIAMOND, ChatColor.GOLD + "(● ˃̶͈̀∀˂̶͈́)੭ु⁾⁾ ＜アジ鯖に投票するのん！", ChatColor.GRAY + "");

		ItemStack notification = ItemHelper.createItem(Material.ITEM_FRAME, ChatColor.GOLD + "お知らせ", ChatColor.GRAY + "");

		ItemStack hogochi = ItemHelper.createItem(Material.STICK, ChatColor.GOLD + "保護地メニュー", ChatColor.GRAY + "・保護地メニューに移動します。", ChatColor.GRAY + "・保護地の上にいた場合そこが操作の対象となります。", ChatColor.GRAY + "・そうでない場合は左クリックで土地を選択します。");

		inventory.setItem(1, status);
		inventory.setItem(3, send);
		inventory.setItem(4, buy);
		inventory.setItem(5, cash);
		inventory.setItem(6, ranking);
		inventory.setItem(7, auction);
		inventory.setItem(10, economy);
		inventory.setItem(12, vote);
		inventory.setItem(13, notification);
		inventory.setItem(14, hogochi);

		//隙間埋め用の色付き羊毛

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(9, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(17, ItemHelper.createSeasonalColorWool1());
	}

	public void update(){
		Inventory inventory = manager.getInventory(Type.HOME_MENU);

		Player player = manager.getPlayer();
		UUID uuid = manager.getUniqueId();

		ItemStack status = inventory.getItem(1);

		ItemHelper.clearLore(status);

		if(HyperingEconomyGUI.isEnableEconomy()){
			ItemHelper.addLore(status, ChatColor.GRAY + "所持金ランキングの順位: " + HyperingEconomyGUI.getGUIListener().getPossesionMoneyRanking().getRank(uuid));
			ItemHelper.addLore(status, ChatColor.GRAY + "");

			HyperingEconomyGUI.getHyperingEconomyAPI().getMoney(player, uuid, new Callback<Result>(){

				@Override
				public void done(Result result) {
					ItemHelper.addLore(status, ChatColor.GRAY + "所持金: ¥" + result.getLong());

					HyperingEconomyGUI.getHyperingEconomyAPI().getNumberOfTickets(player, uuid, new Callback<Result>(){

						@Override
						public void done(Result result) {
							ItemHelper.addLore(status, ChatColor.GRAY + "チケット: " + result.getLong() + "枚");
						}

					});
				}

			});
		}else{
			HyperingEconomyGUI.getHyperingEconomyAPI().getNumberOfTickets(player, uuid, new Callback<Result>(){

				@Override
				public void done(Result result) {
					ItemHelper.addLore(status, ChatColor.GRAY + "チケット: " + result.getLong() + "枚");
				}

			});
		}

		ItemStack hogochiMenu = inventory.getItem(14);

		if(canOpenHogochiMenu()){
			if(manager.getHogochiMenu().isUseWorldGuard()){
				hogochiMenu.setType(Material.WOOD_AXE);
			}else{
				hogochiMenu.setType(Material.GOLD_SPADE);
			}
		}else{
			hogochiMenu.setType(Material.STICK);
		}
	}

	public boolean canOpenHogochiMenu(){
		String worldName = manager.getPlayer().getWorld().getName();
		return worldName.equals("main") || worldName.equals("world_nether") || worldName.equals("world_the_end") || worldName.equals("main_flat");
	}

	public void pushIcon(int slotNumber){
		switch(slotNumber){
		case 3:
			if(!HyperingEconomyGUI.isEnableEconomy()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			NumberScanner smNumberScanner = manager.getNumberScanner();

			smNumberScanner.setDisplayYenMark(true);

			manager.setCase(Case.SEND_MONEY);
			manager.displayGUI(Type.NUMBER_SCANNER);
			break;
		case 4:
			if(!HyperingEconomyGUI.isEnableEconomy()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			NumberScanner btNumberScanner = manager.getNumberScanner();

			btNumberScanner.setDisplayYenMark(false);

			manager.setCase(Case.BUY_TICKET);
			manager.displayGUI(Type.NUMBER_SCANNER);
			break;
		case 5:
			if(!HyperingEconomyGUI.isEnableEconomy()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			NumberScanner ctNumberScanner = manager.getNumberScanner();

			ctNumberScanner.setDisplayYenMark(false);

			manager.setCase(Case.CASH_TICKET);
			manager.displayGUI(Type.NUMBER_SCANNER);
			break;
		case 6:
			if(!HyperingEconomyGUI.isEnableEconomy()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			manager.displayGUI(Type.POSSESSION_MONEY_RANKING);
			break;
		case 7:
			manager.displayGUI(Type.AUCTION_MENU);
			break;
		case 12:
			TextComponent component = new TextComponent(ChatColor.GOLD + "URL: https://minecraft.jp/servers/azisaba.net/vote");

			component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.jp/servers/azisaba.net/vote"));

			manager.getPlayer().spigot().sendMessage(ChatMessageType.CHAT, component);

			Util.success(Message.VOTE_ON_JPMCS, Material.RAW_FISH, manager.getPlayer());

			manager.close();
			break;
		case 13:
			manager.displayGUI(Type.NOTIFICATION);
			break;
		case 14:
			if(!HyperingEconomyGUI.isEnableEconomy()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			HogochiMenu hogochiMenu = manager.getHogochiMenu();

			if(!canOpenHogochiMenu()){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				break;
			}

			Meta.removeMeta(manager.getPlayer());

			hogochiMenu.update();

			manager.displayGUI(Type.HOGOCHI_MENU);
			break;
		default:
			break;
		}
	}*/

}
