package amata1219.hypering.economy.gui.util;

import org.bukkit.ChatColor;

public class Message {

	public static final String VOTE_ON_JPMCS = ChatColor.GOLD + "アジ鯖 | JPMCS\n" +  ChatColor.GRAY + "投票URLを表示しました。";

	public static final String COMPLETED = ChatColor.GOLD + "完了 | ";
	public static final String SUCCESS = ChatColor.GOLD + "成功 |";
	public static final String CONFIRM = ChatColor.GREEN + "確認 | ";
	public static final String SELECT = ChatColor.AQUA + "選択 | ";
	public static final String INPUT = ChatColor.AQUA + "選択 | ";
	public static final String FAILED = ChatColor.RED + "失敗 | ";
	public static final String WARN = ChatColor.RED + "警告 | ";

	public static final String CAN_NOT_USE_THIS_FUNCATION = ChatColor.GRAY + "このサーバーでは使用出来ません。";

	public static final String IS_MIN_VALUE = ChatColor.GRAY + "これ以上削除出来ません。";
	public static final String IS_MAX_VALUE = ChatColor.GRAY + "これ以上入力出来ません。";
	public static final String NOT_ALLOWED_ZERO = ChatColor.GRAY + "0を指定する事は出来ません。";
	public static final String NOT_ENOUGH_POSSESSION_MONEY = ChatColor.GRAY + "所持金が足りません";
	public static final String NOT_ENOUGH_POSSESSION_TICKET = ChatColor.GRAY + "所持チケットが足りません。";
	public static final String IS_MIN_LENGTH = ChatColor.GRAY + "これ以上削除出来ません。";
	public static final String IS_MAX_LENGTH = ChatColor.GRAY + "これ以上入力出来ません。";
	public static final String TRANSMISSION_DESTINATION_IS_NOT_EXIST = ChatColor.GRAY + "送信相手が不明です。";

	public static final String LEFT_CLICK_TO_SELECT_HOGOCHI = ChatColor.GRAY + "左クリックで土地を選択して下さい。";
	public static final String TRY_AGAIN_FROM_HOGOCHI_MENU = ChatColor.GRAY + "保護地メニューからやり直して下さい。";
	public static final String TOO_MANY_HOGOCHI = ChatColor.GRAY + "作成された保護数が多すぎます。";
	public static final String IS_NOT_SELECTED = ChatColor.GRAY + "結合する土地を選択して下さい。";
	public static final String IS_DIAGONAL= ChatColor.GRAY + "斜めの結合は出来ません。";
	public static final String IS_L = ChatColor.GRAY + "L字の結合は出来ません。";
	public static final String CONTAINS_NULL_HOGOCHI = ChatColor.GRAY + "存在しない土地が含まれています。";
	public static final String CONTAINS_OTHER_PLAYERS_HOGOCHI = ChatColor.GRAY + "他プレイヤーの土地が含まれています。";
	public static final String NEED_MANY_STEP = ChatColor.GRAY + "2個より多く土地を選択する事は出来ません。";
	public static final String NOT_SOLD_THIS_HOGOCHI = ChatColor.GRAY + "この土地は販売されていません。";
	public static final String CAN_NOT_BUY_MY_HOGOCHI = ChatColor.GRAY + "自分の土地は購入出来ません。";
	public static final String NOT_ENOUGH_CLAIM_BLOCKS = ChatColor.GRAY + "保護可能ブロック数が足りません。";
	public static final String CAN_NOT_SELL_OTHER_PLAYERS_HOGOCHI = ChatColor.GRAY + "他プレイヤーの土地は販売出来ません。";
	public static final String THIS_HOGOCHI_ALREADY_WAS_SOLD = ChatColor.GRAY + "この土地は既に販売されています。";
	public static final String CAN_NOT_WITHDRAW_OTHER_PLAYERS_HOGOCHI_SALE = ChatColor.GRAY + "他プレイヤーの土地販売は撤回出来ません。";
	public static final String CAN_NOT_FLATTEN_OTHER_PLAYERS_HOGOCHI = ChatColor.GRAY + "他プレイヤーの土地は更地化出来ません。";
	public static final String CAN_NOT_FLATTEN_HOGOCHI_SALE = ChatColor.GRAY + "販売中の土地は更地化出来ません。";
	public static final String CAN_NOT_COMBINE_OTHER_PLAYERS_HOGOCHI = ChatColor.GRAY + "他プレイヤーの土地は結合出来ません。";
	public static final String CAN_NOT_COMBINE_HOGOCHI_SALE = ChatColor.GRAY + "販売中の土地は結合出来ません。";
	public static final String CAN_NOT_COMBINE_ADMIN_REGION = ChatColor.GRAY + "この土地は購入していません。";
	public static final String THIS_HOGOCHI_IS_MAX_SIZE = ChatColor.GRAY + "この土地はこれ以上結合出来ません。";
	public static final String THIS_HOGOCHI_IS_STANDARD = ChatColor.GRAY + "基準となる土地は外せません。";
	public static final String THIS_HOGOCHI_IS_NOT_PROTECTED = ChatColor.GRAY + "この土地は保護されていません。";
	public static final String CAN_NOT_SPLIT_OTHER_PLAYERS_HOGOCHI = ChatColor.GRAY + "他プレイヤーの土地は分割出来ません。";
	public static final String CAN_NOT_SPLIT_HOGOCHI_SALE = ChatColor.GRAY + "販売中の土地は分割出来ません。";
	public static final String CAN_NOT_SPLIT_SMALL_CLAIM = ChatColor.GRAY + "最小サイズの土地は分割出来ません。";

	public static final String IS_NOT_EXIST_HOGOCHI = ChatColor.GRAY + "選択された場所に土地は存在しません。";
	public static final String HOGOCHI_WAS_DELETED = ChatColor.GRAY + "この土地は削除されました。 ";
	public static final String OWNER_WITHDRAWED_HOGOCHI_SALE = ChatColor.GRAY + "この土地の販売は撤回されました。";
	public static final String HOGOCHI_WAS_BOUGHT = ChatColor.GRAY + "この土地は他プレイヤーに購入されました。";

	public static final String CASE_NOT_FOUND_EXCEPTION = ChatColor.DARK_RED + "エラー | CaseNotFoundException\n" + ChatColor.GRAY + "GUIを閉じて操作し直して下さい。";

	public static String caseToString(Case cs){
		if(cs == null)
			return "";

		switch(cs){
		case SEND_MONEY:
			return "送金";
		case BUY_TICKET:
			return "チケットの購入";
		case CASH_TICKET:
			return "チケットの換金";
		case BUY_HOGOCHI:
			return "土地の購入";
		case SELL_HOGOCHI:
			return "土地の販売";
		case WITHDRAW_HOGOCHI_SALE:
			return "土地販売の撤回";
		case FLATTEN_HOGOCHI:
			return "土地の更地化";
		case CONFIRMATION_FLATTEN_HOGOCHI:
			return "土地の更地化";
		case COMBINE_HOGOCHIES:
			return "土地の結合";
		case SPLIT_HOGOCHI:
			return "土地の分割";
		default:
			//到達しない
			return null;
		}
	}

}
