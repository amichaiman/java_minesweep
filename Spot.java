
public class Spot {
	private boolean isBomb;
	private int numberOfNeighborBombs;
	private boolean isVisible;
	
	public Spot() {
		isBomb = false;
		numberOfNeighborBombs = 0;
		isVisible = false;
	}
	
	public void setBombStatus(boolean status) {
		isBomb = status;
	}
	
	public void setNumberOfNeighborBombs(int numberOfNeighborBombs) {
		this.numberOfNeighborBombs = numberOfNeighborBombs;
	}
	
	public void setVisibility(boolean status) {
		isVisible = status;
	}
	
	public boolean getBombStatus() {
		return isBomb;
	}
	
	public int getNumberOfNeighborBombs() {
		return numberOfNeighborBombs;
	}
	
	public boolean getVisibilityStatus() {
		return isVisible;
	}
	
}
