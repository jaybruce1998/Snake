import java.util.*;
import javax.swing.*;
import java.awt.event.*;
public class Snake extends KeyAdapter
{
	private class Pair
	{
		private int a, b;
		private Pair(int a, int b)
		{
			this.a=a;
			this.b=b;
		}
		private Pair(Pair p, int a, int b)
		{
			this(p.a+a, p.b+b);
		}
	}
	private javax.swing.Timer timer;
	private Deque<Pair> snake;
	private char[][] board;
	private int x, y, score;
	private boolean move;
	private String dashes;
	public Snake(int size)
	{
		final int delay=250;
		ActionListener taskPerformer=new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				move();
			}
		};
		timer=new javax.swing.Timer(delay, taskPerformer);
		board=new char[size][size];
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				board[i][j]=' ';
		board[0][0]='S';
		snake=new ArrayDeque<>();
		snake.add(new Pair(0, 0));
		x=1;
		y=0;
		score=-1;
		move=true;
		dashes="<br/>"+String.join("", Collections.nCopies(size, "-"))+"<br/>";
		place();
	}
	private void place()
	{
		List<Pair> open=new ArrayList<>();
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board.length; j++)
				if(board[i][j]==' ')
					open.add(new Pair(i, j));
		if(open.isEmpty())
			return;
		Pair p=open.get((int)(Math.random()*open.size()));
		board[p.a][p.b]='F';
		score++;
	}
	private void move()
	{
		Pair p=new Pair(snake.peekFirst(), y, x);
		if(p.a<0||p.a>=board.length||p.b<0||p.b>=board.length)
		{
			move=false;
			return;
		}
		char c=board[p.a][p.b];
		if(c=='S')
		{
			move=false;
			return;
		}
		board[p.a][p.b]='S';
		snake.addFirst(p);
		if(c=='F')
			place();
		else
		{
			p=snake.pollLast();
			board[p.a][p.b]=' ';
		}
	}
	private void turn(int x, int y)
	{
		this.x=x;
		this.y=y;
		move();
		timer.restart();
	}
	public void keyPressed(KeyEvent k)
	{
		int c=k.getKeyCode();
		if(c==KeyEvent.VK_UP&&y==0)
			turn(0, -1);
		else if(c==KeyEvent.VK_DOWN&&y==0)
			turn(0, 1);
		else if(c==KeyEvent.VK_LEFT&&x==0)
			turn(-1, 0);
		else if(c==KeyEvent.VK_RIGHT&&x==0)
			turn(1, 0);
	}
	public String toString()
	{
		String r="<html><tt>Score: "+score+dashes;
		for(char[] row: board)
			r+=new String(row).replaceAll(" ", "&nbsp;")+"<br/>";
		return r+"</tt></html>";
	}
	public int play()
	{
		JFrame frame=new JFrame();
		frame.addKeyListener(this);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel j=new JLabel(toString());
		frame.add(j);
		frame.pack();
		timer.start();
		while(move)
			j.setText(toString());
		frame.dispose();
		return score;
	}
	public static void main(String[] a)
	{
		for(int p=JOptionPane.YES_OPTION, score; p==JOptionPane.YES_OPTION; p=JOptionPane.showConfirmDialog(null,
			"You got "+score+" points. Play again?", "Snake", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
			score=new Snake(15).play();
		System.exit(0);
	}
}