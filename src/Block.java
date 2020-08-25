import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chunchang
 *
 */
public class Block implements IBlock{

	private Point topLeft;
	private Point botRight;

	private Color color;

	private int depth;

	private IBlock topLeftTree;
	private IBlock topRightTree;
	private IBlock botLeftTree;
	private IBlock botRightTree;

	private IBlock parent;

	private int index;

	/**
	 * constructor Block
	 * @param TL
	 * @param BR
	 * @param c
	 */
	public Block(Point TL, Point BR, Color c)
	{
		this.topLeft = TL;
		this.botRight = BR;
		this.color = c;
	}
	
	public Block(Point TL, Point BR, int depth, Block p)
	{
		this.topLeft = TL;
		this.botRight = BR;
		this.parent = p;
		this.depth = depth;
	}
	
	public Block(Point TL, Point BR, int depth, Block p, Color c)
	{
		this.topLeft = TL;
		this.botRight = BR;
		this.parent = p;
		this.color = c;
		this.depth = depth;

	}

	@Override
	public int depth() {
		return this.depth;
	}

	public void setdepth(int i)
	{
		this.depth = i;
	}

	@Override
	public void smash(int maxDepth) {

		/*
		 * p1 -- p2 --
		 * |     |     |
		 * p3 -- p4 -- p5
		 * |     |     |
		 *    -- p6 -- p7
		 */
		if(this.depth < maxDepth && isleaf())
		{
			// the block is not in max depth and it's leaf node
			// create four new nodes and assign four their parents to this block
			this.color = null;

			int X_length = this.botRight.getX() - this.topLeft.getX();
			int Y_length = this.botRight.getY() - this.topLeft.getY();


			Point p1 = this.topLeft;
			Point p2 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY());
			Point p3 = new Point(this.topLeft.getX(), this.topLeft.getY() + Y_length / 2);
			Point p4 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length / 2);
			Point p5 = new Point(this.topLeft.getX() + X_length, this.topLeft.getY() + Y_length / 2);
			Point p6 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length);
			Point p7 = this.botRight;

			Random rand = new Random();

			this.topLeftTree = new Block(p1, p4, this.depth + 1, this, IBlock.COLORS[rand.nextInt(IBlock.COLORS.length)]);
			this.topRightTree = new Block(p2, p5, this.depth + 1, this, IBlock.COLORS[rand.nextInt(IBlock.COLORS.length)]);
			this.botLeftTree = new Block(p3, p6, this.depth + 1, this, IBlock.COLORS[rand.nextInt(IBlock.COLORS.length)]);
			this.botRightTree = new Block(p4, p7, this.depth + 1, this, IBlock.COLORS[rand.nextInt(IBlock.COLORS.length)]);
		}


	}

	@Override
	public List<IBlock> children() {
		ArrayList<IBlock> clist = new ArrayList<IBlock>();
		if(isleaf())
		{
			return clist;
		}
		clist.add(this.topLeftTree);
		clist.add(this.topRightTree);
		clist.add(this.botRightTree);
		clist.add(this.botLeftTree);
		return clist;
	}

	@Override
	public void rotate() {
		//change coordinates
		if(!isleaf()) // have four children
		{
			//update new coordinate until reach leaf node
			
			int X_length = this.botRight.getX() - this.topLeft.getX();
			int Y_length = this.botRight.getY() - this.topLeft.getY();
			
			Point p1 = this.topLeft;
			Point p2 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY());
			Point p3 = new Point(this.topLeft.getX(), this.topLeft.getY() + Y_length / 2);
			Point p4 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length / 2);
			Point p5 = new Point(this.topLeft.getX() + X_length, this.topLeft.getY() + Y_length / 2);
			Point p6 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length);
			Point p7 = this.botRight;
			
			/*
			 * p1 -- p2 --
			 * |     |     |
			 * p3 -- p4 -- p5
			 * |     |     |
			 *    -- p6 -- p7
			 *    
			 *    botleft(p3, p6) -> topleft(p1, p4)
			 *    topleft(p1, p4) -> topright(p2, p5) 
			 *    topright(p2, p5) -> botright(p4, p7)
			 *    botright(p4, p7) -> botleft(p3, p6)
			 */
			
		
			IBlock temp = new Block(p1, p3, 0, null, null);
			temp = this.botLeftTree;
			((Block)this.botRightTree).setTL(p3);
			((Block)this.botRightTree).setBR(p6);
			this.botLeftTree = this.botRightTree;
			((Block)this.botLeftTree).updateCord();
			
			((Block)this.topRightTree).setTL(p4);
			((Block)this.topRightTree).setBR(p7);
			this.botRightTree = this.topRightTree;
			((Block)this.botRightTree).updateCord();

			((Block)this.topLeftTree).setTL(p2);
			((Block)this.topLeftTree).setBR(p5);
			this.topRightTree = this.topLeftTree;
			((Block)this.topRightTree).updateCord();

			((Block)temp).setTL(p1);
			((Block)temp).setBR(p4);
			this.topLeftTree = temp;
			((Block)this.topLeftTree).updateCord();

		}

	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setColor(Color c) {
		this.color = c;

	}

	@Override
	public Point getTopLeft() {
		return this.topLeft;
	}

	@Override
	public Point getBotRight() {
		return this.botRight;
	}

	@Override
	public boolean isleaf() {
		return (this.botRightTree == null && this.botLeftTree == null && this.topLeftTree == null && this.topRightTree == null);
	}

	@Override
	public IBlock getTopLeftTree() {
		return this.topLeftTree;
	}

	@Override
	public IBlock getTopRightTree() {
		return this.topRightTree;
	}

	@Override
	public IBlock getBotLeftTree() {
		return this.botLeftTree;
	}

	@Override
	public IBlock getBotRightTree() {
		return this.botRightTree;
	}

	public void setTL(int x, int y)
	{
		this.topLeft = new Point(x, y);
	}

	public void setBR(int x, int y)
	{
		this.botRight = new Point(x, y);
	}
	
	public void setTL(Point TL)
	{
		this.topLeft = TL;
	}

	public void setBR(Point BR)
	{
		this.botRight = BR;
	}

	public void setIndex(int i)
	{
		this.index = i;
	}

	public int getIndex()
	{
		return this.index;
	}
	
	public void setParent(IBlock p)
	{
		this.parent = p;
	}
	
	public IBlock getParent()
	{
		return this.parent;
	}
	
	/**
	 * 
	 */
	public void updateCord()
	{
		if(!isleaf()) // four children
		{
			int X_length = this.botRight.getX() - this.topLeft.getX();
			int Y_length = this.botRight.getY() - this.topLeft.getY();
			
			//update new coordinate and recursively goes to subtrees to update their coordinate 
			Point p1 = this.topLeft;
			Point p2 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY());
			Point p3 = new Point(this.topLeft.getX(), this.topLeft.getY() + Y_length / 2);
			Point p4 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length / 2);
			Point p5 = new Point(this.topLeft.getX() + X_length, this.topLeft.getY() + Y_length / 2);
			Point p6 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length);
			Point p7 = this.botRight;
			
			/*
			 * p1 -- p2 --
			 * |     |     |
			 * p3 -- p4 -- p5
			 * |     |     |
			 *    -- p6 -- p7
			 *    
			 *    botleft(p3, p6) -> topleft(p1, p4)
			 *    topleft(p1, p4) -> topright(p2, p5) 
			 *    topright(p2, p5) -> botright(p4, p7)
			 *    botright(p4, p7) -> botleft(p3, p6)
			 */
			
			((Block)this.botLeftTree).setTL(p3);
			((Block)this.botLeftTree).setBR(p6);
			((Block)this.botLeftTree).updateCord();

			
			((Block)this.botRightTree).setTL(p4);
			((Block)this.botRightTree).setBR(p7);
			((Block)this.botRightTree).updateCord();

			((Block)this.topRightTree).setTL(p2);
			((Block)this.topRightTree).setBR(p5);
			((Block)this.topRightTree).updateCord();

			((Block)this.topLeftTree).setTL(p1);
			((Block)this.topLeftTree).setBR(p4);
			((Block)this.topLeftTree).updateCord();
		}
	}
	
	/**
	 * if the position matches, update the coordinate and recursively update their subtree
	 * @param ch
	 */
	public void updateChild(IBlock ch)
	{
		int X_length = this.botRight.getX() - this.topLeft.getX();
		int Y_length = this.botRight.getY() - this.topLeft.getY();
		
		Point p1 = this.topLeft;
		Point p2 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY());
		Point p3 = new Point(this.topLeft.getX(), this.topLeft.getY() + Y_length / 2);
		Point p4 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length / 2);
		Point p5 = new Point(this.topLeft.getX() + X_length, this.topLeft.getY() + Y_length / 2);
		Point p6 = new Point(this.topLeft.getX() + X_length / 2, this.topLeft.getY() + Y_length);
		Point p7 = this.botRight;
		
		/*
		 * p1 -- p2 --
		 * |     |     |
		 * p3 -- p4 -- p5
		 * |     |     |
		 *    -- p6 -- p7
		 *    
		 *    botleft(p3, p6) -> topleft(p1, p4)
		 *    topleft(p1, p4) -> topright(p2, p5) 
		 *    topright(p2, p5) -> botright(p4, p7)
		 *    botright(p4, p7) -> botleft(p3, p6)
		 */
		
		Block dummyTF = new Block(p1, p4, 0, null, null);
		Block dummyTR = new Block(p2, p5, 0, null, null);
		Block dummyBR = new Block(p4, p7, 0, null, null);
		Block dummyBF = new Block(p3, p6, 0, null, null);

		
		
		if(dummyTF.equalpos(ch))
		{
			this.topLeftTree = ch;
		}
		else if(dummyTR.equalpos(ch))
		{
			this.topRightTree = ch;
		}
		else if(dummyBR.equalpos(ch))
		{
			this.botRightTree = ch;
		}
		else if(dummyBF.equalpos(ch))
		{
			this.botLeftTree = ch;
		}
	}
	
	public void setTopLeftTree(IBlock topLeftTree) {
		this.topLeftTree = topLeftTree;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public void setBotRight(Point botRight) {
		this.botRight = botRight;
	}

	public void setTopRightTree(IBlock topRightTree) {
		this.topRightTree = topRightTree;
	}

	public void setBotLeftTree(IBlock botLeftTree) {
		this.botLeftTree = botLeftTree;
	}

	public void setBotRightTree(IBlock botRightTree) {
		this.botRightTree = botRightTree;
	}
	
	/**
	 * @param o
	 * @return if the topleft and bottom right blocks match, return true
	 */
	public boolean equalpos(IBlock o) {
		return  (this.getTopLeft().getX() == o.getTopLeft().getX() && this.getTopLeft().getY() == o.getTopLeft().getY()
				&& this.getBotRight().getX() == o.getBotRight().getX() && this.getBotRight().getY() == o.getBotRight().getY());
	}

	@Override
	public boolean equals(IBlock o) {
		
		boolean parentcheck = this.getParent() == ((Block)o).getParent();
		
		List<IBlock> chs = this.children();
		List<IBlock> ochs = o.children();
		boolean childrencheck = true;
		
		for(int i = 0; i < chs.size(); i++)
		{
			if(chs.get(i) != ochs.get(i))
			{
				childrencheck = false;
				break;
			}
				
		}
		
		return  parentcheck && childrencheck;
	}


}
