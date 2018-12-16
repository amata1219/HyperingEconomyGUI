package amata1219.hypering.economy.gui.home;

public class OldGUIManager {

/*	private UUID uuid;
	private HashMap<Type, Inventory> guis = new HashMap<>();

	private OldHomeMenu homeMenu;
	private NumberScanner numberScanner;
	private CharacterScanner characterScanner;
	private Confirmation confirmation;

	private HogochiMenu hogochiMenu;
	private CombineProtectedRegions combineHogochies;
	private SplitRegion splitHogochi;

	private AuctionMenu auctionMenu;
	private Exhibit exhibit;
	private MyAuctionList myAuctionList;

	private Case cs;

	private OfflinePlayer inputtedPlayer;
	private long inputtedNumber;

	private Claim inputtedClaim;
	private ProtectedRegion inputtedRegion;

	private boolean cooldown;

	private OldGUIManager(){

	}

	public static OldGUIManager load(Player player){
		OldGUIManager manager = new OldGUIManager();

		manager.uuid = player.getUniqueId();

		manager.initialize();

		return manager;
	}

	public void initialize(){
		HashMap<Type, Inventory> guis = this.guis;

		guis.put(Type.HOME_MENU, ItemHelper.createInventory(18, Type.HOME_MENU));
		guis.put(Type.NUMBER_SCANNER, ItemHelper.createInventory(18, Type.NUMBER_SCANNER));
		guis.put(Type.CHARACTER_SCANNER, ItemHelper.createInventory(45, Type.CHARACTER_SCANNER));
		guis.put(Type.CONFIRMATION, ItemHelper.createInventory(9, Type.CONFIRMATION));
		guis.put(Type.HOGOCHI_MENU, ItemHelper.createInventory(9, Type.HOGOCHI_MENU));
		guis.put(Type.COMBINE_HOGOCHIES, ItemHelper.createInventory(18, Type.COMBINE_HOGOCHIES));
		guis.put(Type.SPLIT_HOGOCHI, ItemHelper.createInventory(9, Type.SPLIT_HOGOCHI));
		guis.put(Type.AUCTION_MENU, ItemHelper.createInventory(9, Type.AUCTION_MENU));
		guis.put(Type.EXHIBIT, ItemHelper.createInventory(9, Type.EXHIBIT));
		guis.put(Type.MY_AUCTION_LIST, ItemHelper.createInventory(9, Type.MY_AUCTION_LIST));

		homeMenu = OldHomeMenu.load(this);
		numberScanner = NumberScanner.load(this);
		characterScanner = CharacterScanner.load(this);
		confirmation = Confirmation.load(this);

		hogochiMenu = HogochiMenu.load(this);
		combineHogochies = CombineProtectedRegions.load(this);
		splitHogochi = SplitRegion.load(this);

		auctionMenu = AuctionMenu.load(this);
		exhibit = Exhibit.load(this);
		myAuctionList = MyAuctionList.load(this);
	}

	public void unload(){

	}

	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public UUID getUniqueId(){
		return uuid;
	}

	public void displayGUI(Type type){
		if(type == null)
			return;

		switch(type){
		case HOME_MENU:
			cs = null;
			homeMenu.update();
			break;
		case NUMBER_SCANNER:
			numberScanner.update();
			break;
		case CHARACTER_SCANNER:
			characterScanner.update();
			break;
		case CONFIRMATION:
			//confirmation.update();
			break;
		case POSSESSION_MONEY_RANKING:
			open(HyperingEconomyGUI.getGUIListener().getPossesionMoneyRanking().getInventory());
			return;
		case NOTIFICATION:
			open(HyperingEconomyGUI.getGUIListener().getNotification().getInventory());
			return;
		case HOGOCHI_MENU:
			hogochiMenu.update();
			break;
		case COMBINE_HOGOCHIES:
			//combineHogochies.update();
			break;
		case SPLIT_HOGOCHI:
			splitHogochi.update();
			break;
		case AUCTION_MENU:
			auctionMenu.update();
			break;
		case EXHIBIT:
			//exhibit.update();
		case MY_AUCTION_LIST:
			myAuctionList.update();
			break;
		default:
			break;
		}

		open(guis.get(type));
	}

	public Inventory getInventory(Type type){
		return guis.get(type);
	}

	public OldHomeMenu getHomeMenu(){
		return homeMenu;
	}

	public NumberScanner getNumberScanner(){
		return numberScanner;
	}

	public CharacterScanner getCharacterScanner(){
		return characterScanner;
	}

	public Confirmation getConfirmation(){
		return confirmation;
	}

	public HogochiMenu getHogochiMenu(){
		return hogochiMenu;
	}

	public CombineProtectedRegions getCombineHogochies() {
		return combineHogochies;
	}

	public SplitRegion getSplitHogochi() {
		return splitHogochi;
	}

	public void open(Inventory inventory){
		if(inventory == null)
			return;

		getPlayer().openInventory(inventory);
	}

	public void close(){
		clear();

		getPlayer().closeInventory();
	}

	public void quietClose(){
		getPlayer().closeInventory();
	}

	public Case getCase() {
		return cs;
	}

	public boolean hasCase(){
		return cs != null;
	}

	public void setCase(Case cs) {
		this.cs = cs;
	}

	public OfflinePlayer getInputtedPlayer() {
		return inputtedPlayer;
	}

	public boolean hasInputtedPlayer(){
		return inputtedPlayer != null;
	}

	public void putPlayer(OfflinePlayer inputtedPlayer) {
		this.inputtedPlayer = inputtedPlayer;
	}

	public void clearInputtedPlayer(){
		inputtedPlayer = null;
	}

	public long getInputtedNumber(){
		return inputtedNumber;
	}

	public boolean hasInputtedNumber(){
		return inputtedNumber != -1;
	}

	public void putNumber(long inputtedNumber){
		this.inputtedNumber = inputtedNumber;
	}

	public void clearInputtedNumber(){
		inputtedNumber = -1;
	}

	public void putClaim(Claim claim){
		this.inputtedClaim = claim;
	}

	public Claim getInputtedClaim(){
		return inputtedClaim;
	}

	public boolean hasInputtedClaim(){
		return inputtedClaim != null;
	}

	public void clearInputtedClaim(){
		inputtedClaim = null;
	}

	public void checkInputtedClaim(Claim claim, String reason){
		if(inputtedClaim == null || !hasCase())
			return;

		if(!inputtedClaim.getID().equals(claim.getID()))
			return;

		close();

		Util.warn(Message.WARN + Message.caseToString(cs) , reason, Util.caseToMaterial(cs), getPlayer());
	}

	public void putRegion(ProtectedRegion region){
		this.inputtedRegion = region;
	}

	public ProtectedRegion getInputtedRegion(){
		return inputtedRegion;
	}

	public boolean hasInputtedRegion(){
		return inputtedRegion != null;
	}

	public void clearInputtedRegion(){
		inputtedRegion = null;
	}

	public void checkInputtedRegion(ProtectedRegion region, String reason){
		if(!hasCase() || inputtedRegion == null)
			return;

		if(!inputtedRegion.getId().equals(region.getId()))
			return;

		close();

		Util.warn(Message.WARN + Message.caseToString(cs), reason, Util.caseToMaterial(cs), getPlayer());
	}

	public String getInputtedHogochiID(){
		if(inputtedClaim != null){
			return String.valueOf(inputtedClaim.getID());
		}else if(inputtedRegion != null){
			return inputtedRegion.getId();
		}
		return "";
	}

	public long getPrice(){
		if(inputtedClaim != null)
			return ClaimByebye.getPrice(inputtedClaim);

		if(inputtedRegion != null)
			return RegionByebye.getPrice(inputtedRegion);

		return -1;
	}

	public long getNumberOfTicket(){
		if(inputtedRegion == null)
			return -1;

		if(RegionByebye.isAdminRegion(inputtedRegion))
			return 160;

		return -1;
	}

	public boolean isCooldown(){
		return cooldown;
	}

	public void setCooldown(long time){
		cooldown = true;

		new BukkitRunnable(){

			@Override
			public void run(){
				cooldown = false;
			}

		}.runTaskLater(HyperingEconomyGUI.getPlugin(), time);
	}

	public void clear(){
		cs = null;

		clearInputtedPlayer();

		clearInputtedNumber();

		clearInputtedClaim();

		clearInputtedRegion();
	}*/

}
