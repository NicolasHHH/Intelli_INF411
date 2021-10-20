package ocean;



/**
 * La classe permettant l'utilisation de nombres entiers comme marques dans un
 * océan. Remplace les marques {@code int} utilisées dans les versions
 * précédentes du TD
 */
public class IntMark implements Mark {
	/**
	 * La valeur de la marque
	 */
	private final int value;
	
	/**
	 * Le nom de la marque
	 */
	private final String text;

	/**
	 * Le constructeur de base
	 * @param value la valeur entière à utiliser pour la marque
	 * @param text la representation textuelle
	 */
	public IntMark(int value, String text) {
		this.value = value;
		this.text = text;
	}

	/**
	 * Le constructeur de base
	 * @param value la valeur entière à utiliser pour la marque
	 */
	public IntMark(int value) {
		this.value = value;
		this.text = Integer.toString(value);
	}
	
	public Integer toInteger() {
		return new Integer(value);
	}
	
	@Override
	public String toString() {
		return text;		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Mark))
			return false;
		
		Mark that = (Mark) obj;
		return this.value == that.toInteger() 
				&& this.text.equals(that.toString());
	}
	
	@Override
	public int hashCode() {
		return value;
	}
}
