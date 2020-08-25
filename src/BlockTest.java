import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class BlockTest {
	
	@Test
	public void testPoint() {
		
		
		Point t = new Point(0 ,0);
		assertTrue(t.toString().equals("x: 0, y: 0"));

	}

	@Test
	public void testBlock() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b ,k);
		Block bl2 = new Block(t, b, 5, bl);
		Block bl3 = new Block(t, b, 5, bl, k);
		
		assertNotNull(bl);
		assertNotNull(bl2);
		assertNotNull(bl3);

	}
	
	@Test
	public void testdepth() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 5, null, k);
		assertEquals(5, bl.getDepth());

		bl.setDepth(4);

		assertEquals(4, bl.getDepth());
		bl.setdepth(3);
		assertEquals(3, bl.getDepth());
		assertEquals(3, bl.depth());

	}
	
	@Test
	public void testsetTLBR() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);
		
		Point tn = new Point(2 ,2);
		Point bn = new Point(7 ,7);
		
		Point tn2 = new Point(3 ,3);
		Point bn2 = new Point(9 ,9);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 5, null, k);
		bl.setTL(1, 1);
		bl.setBR(8, 8);

		assertTrue(bl.getTopLeft().getX() == 1);
		assertTrue(bl.getTopLeft().getY() == 1);
		assertTrue(bl.getBotRight().getX() == 8);
		assertTrue(bl.getBotRight().getY() == 8);

		bl.setTL(tn);
		bl.setBR(bn);
		
		assertTrue(bl.getTopLeft().getX() == 2);
		assertTrue(bl.getTopLeft().getY() == 2);
		assertTrue(bl.getBotRight().getX() == 7);
		assertTrue(bl.getBotRight().getY() == 7);
		
		bl.setTopLeft(tn2);
		bl.setBotRight(bn2);
		
		assertTrue(bl.getTopLeft().getX() == 3);
		assertTrue(bl.getTopLeft().getY() == 3);
		assertTrue(bl.getBotRight().getX() == 9);
		assertTrue(bl.getBotRight().getY() == 9);
	}
	
	@Test
	public void testParent() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);
		
		Point tn = new Point(2 ,2);
		Point bn = new Point(7 ,7);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 5, null, k);
		Block blp = new Block(t, b, 5, null, k);
		
		assertNull(bl.getParent());
		bl.setParent(blp);
		assertTrue(blp == bl.getParent());
	}
	
	@Test
	public void testindex() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 5, null, k);
		assertEquals(0, bl.getIndex());
		bl.setIndex(5);
		assertEquals(5, bl.getIndex());

	}
	
	@Test
	public void testcolor() {
		
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 5, null, null);
		
		assertNull( bl.getColor());
		bl.setColor(k);
		assertTrue(bl.getColor().equals(Color.WHITE));

	}
	
	
	
	@Test
	public void testEqualpos() {
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		Color blue = Color.BLUE;
		
		Block bl = new Block(t, b, 0, null, k);
		Block bl2 = new Block(t, b, 0, bl, blue);
		
		assertTrue(bl.equalpos(bl2));
	}
	
	@Test
	public void testIsleaf() {
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 0, null, k);
		assertNotNull(bl);
		assertTrue(bl.isleaf());
		assertEquals(0, bl.children().size());
	}
	
	@Test
	public void testSmash() {
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 0, null, k);
		bl.smash(1);
		assertTrue(bl.getColor() == null);
		IBlock otl = bl.getTopLeftTree();
		IBlock otr = bl.getTopRightTree();
		IBlock obr = bl.getBotRightTree();
		IBlock obl = bl.getBotLeftTree();
		assertNotNull(bl);
		assertEquals(4, bl.children().size());
		assertEquals(bl, ((Block)otl).getParent());
		assertEquals(bl, ((Block)otr).getParent());
		assertEquals(bl, ((Block)obr).getParent());
		assertEquals(bl, ((Block)obl).getParent());

	}
	
	@Test
	public void testRotate() {
		
		Point t = new Point(0 ,0);
		Point b = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t, b, 0, null, k);
		bl.smash(1);
		IBlock otl = bl.getTopLeftTree();
		IBlock otr = bl.getTopRightTree();
		IBlock obr = bl.getBotRightTree();
		IBlock obl = bl.getBotLeftTree();
		
		bl.rotate();
		
		assertTrue(bl.getTopLeftTree().getColor().equals(obl.getColor()));
		assertTrue(bl.getTopRightTree().getColor().equals(otl.getColor()));
		assertTrue(bl.getBotRightTree().getColor().equals(otr.getColor()));
		assertTrue(bl.getBotLeftTree().getColor().equals(obr.getColor()));
		
		IBlock ntl = bl.getTopLeftTree();
		IBlock ntr = bl.getTopRightTree();
		IBlock nbr = bl.getBotRightTree();
		IBlock nbl = bl.getBotLeftTree();

		assertEquals(bl, ((Block)ntl).getParent());
		assertEquals(bl, ((Block)ntr).getParent());
		assertEquals(bl, ((Block)nbr).getParent());
		assertEquals(bl, ((Block)nbl).getParent());
	}
	
	@Test
	public void testupdateCord() {
		
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
		
		Point t1 = new Point(0 ,0);
		Point t2 = new Point(2 ,0);
		Point t3 = new Point(0 ,2);
		Point t4 = new Point(2 ,2);
		Point t5 = new Point(4 ,2);
		Point t6 = new Point(2 ,4);
		Point t7 = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Point tn = new Point(0 ,0);
		Point bn = new Point(8 ,8);
		
		Block bl = new Block(t1, t7, 0, null, k);
		bl.smash(1);
		
		Block tls = new Block(t1, t4, 0, null, k);
		Block trs = new Block(t2, t5, 0, null, k);
		Block brs = new Block(t4, t7, 0, null, k);
		Block bls = new Block(t3, t6, 0, null, k);
		
		IBlock otl = bl.getTopLeftTree();
		IBlock otr = bl.getTopRightTree();
		IBlock obr = bl.getBotRightTree();
		IBlock obl = bl.getBotLeftTree();
		
		assertTrue(((Block)otl).equalpos(tls));
		assertTrue(((Block)otr).equalpos(trs));
		assertTrue(((Block)obr).equalpos(brs));
		assertTrue(((Block)obl).equalpos(bls));

		
		bl.setTL(tn);
		bl.setBR(bn);
		
		Point t1n = new Point(0 ,0);
		Point t2n = new Point(4 ,0);
		Point t3n = new Point(0 ,4);
		Point t4n = new Point(4 ,4);
		Point t5n = new Point(8 ,4);
		Point t6n = new Point(4 ,8);
		Point t7n = new Point(8 ,8);
		
		Block tln = new Block(t1n, t4n, 0, null, k);
		Block trn = new Block(t2n, t5n, 0, null, k);
		Block brn = new Block(t4n, t7n, 0, null, k);
		Block bln = new Block(t3n, t6n, 0, null, k);

		bl.updateCord();
		
		
		IBlock ntl = bl.getTopLeftTree();
		IBlock ntr = bl.getTopRightTree();
		IBlock nbr = bl.getBotRightTree();
		IBlock nbl = bl.getBotLeftTree();
		
		assertTrue(((Block)ntl).equalpos(tln));
		assertTrue(((Block)ntr).equalpos(trn));
		assertTrue(((Block)nbr).equalpos(brn));
		assertTrue(((Block)nbl).equalpos(bln));

	}
	
	@Test
	public void testupdateChild() {
		
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
		
		Point t1 = new Point(0 ,0);
		Point t2 = new Point(2 ,0);
		Point t3 = new Point(0 ,2);
		Point t4 = new Point(2 ,2);
		Point t5 = new Point(4 ,2);
		Point t6 = new Point(2 ,4);
		Point t7 = new Point(4 ,4);

		Color k = Color.WHITE;
		
		Block bl = new Block(t1, t7, 0, null, k);
		bl.smash(1);
		
		Block tls = new Block(t1, t4, 0, null, Color.CYAN);
		Block trs = new Block(t2, t5, 0, null, Color.YELLOW);
		Block brs = new Block(t4, t7, 0, null, Color.GRAY);
		Block bls = new Block(t3, t6, 0, null, Color.GREEN);

		bl.updateChild(tls);
		bl.updateChild(trs);
		bl.updateChild(brs);
		bl.updateChild(bls);

		
		IBlock ntl = bl.getTopLeftTree();
		IBlock ntr = bl.getTopRightTree();
		IBlock nbr = bl.getBotRightTree();
		IBlock nbl = bl.getBotLeftTree();
		
		assertTrue(((Block)ntl).getColor().equals(Color.CYAN));
		assertTrue(((Block)ntr).getColor().equals(Color.YELLOW));
		assertTrue(((Block)nbr).getColor().equals(Color.GRAY));
		assertTrue(((Block)nbl).getColor().equals(Color.GREEN));

	}
	
	@Test
	public void testequals() {
		
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
		
		Point t1 = new Point(0 ,0);
		Point t2 = new Point(2 ,0);
		Point t3 = new Point(0 ,2);
		Point t4 = new Point(2 ,2);
		Point t5 = new Point(4 ,2);
		Point t6 = new Point(2 ,4);
		Point t7 = new Point(4 ,4);
		
		Block bl = new Block(t1, t7, 0, null, null);
		
		Block tls = new Block(t1, t4, 0, null, Color.CYAN);
		Block trs = new Block(t2, t5, 0, null, Color.YELLOW);
		Block brs = new Block(t4, t7, 0, null, Color.GRAY);
		Block bls = new Block(t3, t6, 0, null, Color.GREEN);
		
		bl.setTopLeftTree(tls);
		bl.setTopRightTree(trs);
		bl.setBotRightTree(brs);
		bl.setBotLeftTree(bls);
		
		Block bl2 = new Block(t1, t7, 0, null, null);
		
		bl2.setTopLeftTree(tls);
		bl2.setTopRightTree(trs);
		bl2.setBotRightTree(brs);
		bl2.setBotLeftTree(bls);

		assertTrue(bl2.equals(bl));
		
		bl2.setTopLeftTree(tls);
		bl2.setTopRightTree(trs);
		bl2.setBotRightTree(brs);
		bl2.setBotLeftTree(null);
		
		assertFalse(bl2.equals(bl));


	}
	
//**********************************************GAME TEST

	@Test
	public void testGame() {
		
		Game g = new Game(2, Color.RED);
		assertNotNull(g);
		assertNotNull(g.getRoot());
		assertEquals(2,g.getMax_depth());
		assertTrue(g.getcolor().equals(Color.RED));
		
	}
	
	@Test
	public void testDepth() {
		
		Game g = new Game(1, Color.RED);
		g.setMax_depth(2);
		assertEquals(2,g.getMax_depth());
		assertEquals(2,g.max_depth());
	}
	
	@Test
	public void testRoot() {
		
		Game g = new Game(1, Color.RED);
		assertNotNull(g);
		assertNotNull(g.getRoot());
		g.setRoot(null);
		assertNull(g.getRoot());
		}
	
	@Test
	public void testGetBlock() {
		
		Game g = new Game(1, Color.RED);
		assertNotNull(g.getBlock(0));
		assertNotNull(g.getBlock(1));
		assertNotNull(g.getBlock(2));
		assertNotNull(g.getBlock(3));
		assertNotNull(g.getBlock(4));
		assertNull(g.getBlock(5));

		g.setRoot(null);
		assertNull(g.getBlock(4));
		assertEquals(0, g.getSizeOfTree());

		}
	
	@Test
	public void testSwap() {
		
		Game g = new Game(1, Color.RED);
		
		IBlock get1 = g.getBlock(1);
		IBlock get2 = g.getBlock(2);
		g.swap(1, 2);

		assertTrue(g.getBlock(1).getColor().equals(get2.getColor()));
		assertTrue(g.getBlock(2).getColor().equals(get1.getColor()));

		}
	
	@Test
	public void testFlatten() {
		
		Game g = new Game(1, Color.RED);
		
		Point t1 = new Point(0 ,0);
		Point t2 = new Point(4 ,0);
		Point t3 = new Point(0 ,4);
		Point t4 = new Point(4 ,4);
		Point t5 = new Point(8 ,4);
		Point t6 = new Point(4 ,8);
		Point t7 = new Point(8 ,8);
		
		Block bl = new Block(t1, t7, 0, null, null);
		
		bl.smash(1);
		
		Block tls = new Block(t1, t4, 0, bl, Color.RED);
		Block trs = new Block(t2, t5, 0, bl, Color.YELLOW);
		Block brs = new Block(t4, t7, 0, bl, Color.GRAY);
		Block bls = new Block(t3, t6, 0, bl, Color.GREEN);
		
		bl.updateChild(tls);
		bl.updateChild(trs);
		bl.updateChild(brs);
		bl.updateChild(bls);

		
		g.setRoot(bl);
		
		IBlock[][] board = g.flatten();
		assertEquals(2, board.length);
		assertEquals(2, board[0].length);
		assertEquals(2, g.perimeter_score());

		g.setCur_score(5);
		assertEquals(5, g.getCur_score());

		}
	
	@Test
	public void testSizeofboard() {
		
		Game g = new Game(1, Color.RED);
		
		
		assertEquals(8, g.getSizeOfBoard());
		g.setSizeOfBoard(10);
		assertEquals(10, g.getSizeOfBoard());
		assertEquals(5, g.getSizeOfTree());
		g.setSizeOfTree(10);

		
		}
	
	@Test
	public void testGameColor() {
		
		Game g = new Game(1, Color.RED);
		
		g.setcolor(Color.BLACK);
		assertTrue(g.getcolor().equals(Color.BLACK));
		
		}
	
}
