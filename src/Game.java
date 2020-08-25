import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author chunchang
 *
 */
public class Game implements IGame {

	private int max_depth;

	private int sizeOfBoard;

	private Color color;
	private IBlock root;

	private int sizeOfTree;

	private int cur_score;

	/**
	 * @param max_d
	 * @param tar_c
	 */
	public Game(int max_d, Color tar_c)
	{
		this.max_depth = max_d;
		this.color = tar_c;

		this.root = random_init();
	}

	@Override
	public int max_depth() {
		return this.max_depth;
	}

	@Override
	public IBlock random_init() {
		// TODO Auto-generated method stub
		Random ran = new Random();
		Point tl = new Point(0, 0);
		this.sizeOfBoard = 8;
		Point br = new Point(this.sizeOfBoard, this.sizeOfBoard);
		this.root = new Block(tl, br, IBlock.COLORS[ran.nextInt(IBlock.COLORS.length)]);
		((Block)this.root).setdepth(0);

		while(true)
		{
			this.sizeOfTree = numberedBlock();
			int targetNodeInd = ran.nextInt(this.sizeOfTree);
			IBlock target = getBlock(targetNodeInd);
			if(!target.isleaf())
			{
				//get another random number
				continue;
			}
			else if(target.isleaf() && target.depth() < this.max_depth)
			{
				//do split
				target.smash(this.max_depth);
			}
			else
			{
				//reach max and change color randomly, break
				target.setColor(IBlock.COLORS[ran.nextInt(IBlock.COLORS.length)]);
				break;
			}
		}

		return this.root;
	}

	@Override
	public IBlock getBlock(int pos) {
		Queue<IBlock> toVisit = new LinkedList<IBlock>();
		if(root == null)
		{
			return null;
		}

		toVisit.add(root);
		Block sentinal = null;
		int sizecount = 0;
		while(!toVisit.isEmpty())
		{
			sentinal = (Block) toVisit.poll();
			
			if(sizecount == pos)// when the position matches return the block
				{
					return sentinal;
				}
			
			sizecount++;

			if(!sentinal.isleaf())
			{
				toVisit.add(sentinal.getTopLeftTree());
				toVisit.add(sentinal.getTopRightTree());
				toVisit.add(sentinal.getBotRightTree());
				toVisit.add(sentinal.getBotLeftTree());
			}
		}
		return null;
	}

	@Override
	public IBlock getRoot() {
		return this.root;
	}

	@Override
	public void swap(int x, int y) {

		IBlock xb = getBlock(x);
		IBlock yb = getBlock(y);

		if(xb.depth() == yb.depth())
		{
			Point temptl = xb.getTopLeft();
			Point tempbr = xb.getBotRight();

			((Block)xb).setTL(yb.getTopLeft());
			((Block)xb).setBR(yb.getBotRight());
			((Block)xb).updateCord();
			IBlock parent1 = ((Block)xb).getParent();

			((Block)yb).setTL(temptl);
			((Block)yb).setBR(tempbr);
			((Block)yb).updateCord();
			IBlock parent2 = ((Block)yb).getParent();

			//x and y changed coordination already
			((Block)yb).setParent(parent1);
			((Block)xb).setParent(parent2);
			((Block)parent1).updateChild(yb);
			((Block)parent2).updateChild(xb);

			//update relationship to parent
		}


	}

	@Override
	public IBlock[][] flatten() {
		int edge = 1;
		int count = 0;
		while(count < this.max_depth)
		{
			edge *= 2;
			count++;
		}

		int unit = this.sizeOfBoard / edge; // size of unit cell

		IBlock[][] scoreBoard = new Block[edge][edge];
		//use BFS, go through every node
		Queue<IBlock> tovisit = new LinkedList<>();
		tovisit.add(root);
		IBlock sentinal = null;
		int score = 0;
		int paint = 0;
		while(!tovisit.isEmpty())
		{
			
			sentinal = tovisit.poll();
			
			if(!sentinal.isleaf())
			{
				tovisit.add(sentinal.getTopLeftTree());
				tovisit.add(sentinal.getTopRightTree());
				tovisit.add(sentinal.getBotRightTree());
				tovisit.add(sentinal.getBotLeftTree());
			}
			else
			{
				//fill the board which created by the length of game board divided by size of unit cell
				Point temptf = sentinal.getTopLeft();
				Point tempbr = sentinal.getBotRight();

				int X_length = tempbr.getX() - temptf.getX();
				int numOfBlockToDraw = X_length / unit;
				int start_x = temptf.getX() / unit;
				int start_y = temptf.getY() / unit;

				//fill the board only if the node is the leaf node
				for(int i = start_x; i < start_x + numOfBlockToDraw; i++)
				{
					for(int j = start_y; j < start_y + numOfBlockToDraw; j++)
					{
						paint++;
						scoreBoard[j][i] = sentinal;
						if(sentinal.getColor() == this.color & (i == 0 || j == 0 || i == edge - 1 || j == edge - 1))
						{
							//if the node is at the corner, count it twice
							if(i == j || (i == 0 && j == edge - 1) || (j == 0 && i == edge - 1))
							{
								score++;
							}
							score++;
						}
					}
				}
			}
		}
		this.cur_score = score;

		//only put leaf node into the board
		return scoreBoard;
	}

	@Override
	public int perimeter_score() {
		numberedBlock();

		IBlock[][] scoreBoard = flatten();

		return this.cur_score;
	}

	@Override
	public void setRoot(IBlock root) {
		this.root = root;

	}

	/**
	 * using bfs to through every node and update its index
	 * @return number of blocks in the tree
	 */
	public int numberedBlock()
	{
		Queue<IBlock> toVisit = new LinkedList<IBlock>();
		if(root == null)
		{
			return 0;
		}

		toVisit.add(root);
		Block sentinal = null;
		int size = 0;
		while(!toVisit.isEmpty())
		{
			sentinal = (Block) toVisit.poll();
			sentinal.setIndex(size);
			if(!sentinal.isleaf())
			{
				toVisit.add(sentinal.getTopLeftTree());
				toVisit.add(sentinal.getTopRightTree());
				toVisit.add(sentinal.getBotRightTree());
				toVisit.add(sentinal.getBotLeftTree());
			}
			size++;
		}
		return size;
	}

	public int getSizeOfTree()
	{
		return numberedBlock();
	}

	public int getMax_depth() {
		return max_depth;
	}

	public void setMax_depth(int max_depth) {
		this.max_depth = max_depth;
	}

	public int getSizeOfBoard() {
		return sizeOfBoard;
	}

	public void setSizeOfBoard(int sizeOfBoard) {
		this.sizeOfBoard = sizeOfBoard;
	}

	public Color getcolor() {
		return color;
	}

	public void setcolor(Color color) {
		this.color = color;
	}

	public int getCur_score() {
		return cur_score;
	}

	public void setCur_score(int cur_score) {
		this.cur_score = cur_score;
	}

	public void setSizeOfTree(int sizeOfTree) {
		this.sizeOfTree = sizeOfTree;
	}	

}
