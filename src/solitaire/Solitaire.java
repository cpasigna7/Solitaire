package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() { //COMPLETE THIS METHOD
		CardNode ptr = deckRear;
		while (ptr.cardValue != 27){
			ptr = ptr.next;
		}
		int x = ptr.cardValue;
		ptr.cardValue = ptr.next.cardValue;
		ptr.next.cardValue = x;
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {// COMPLETE THIS METHOD
		CardNode ptr = deckRear;
		while (ptr.cardValue != 28){
			ptr = ptr.next;
		}
		int x = ptr.cardValue;
		ptr.cardValue = ptr.next.next.cardValue;
		ptr.next.next.cardValue = x;
		int y = ptr.next.cardValue;
		ptr.next.cardValue = ptr.cardValue;
		ptr.cardValue = y;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {// COMPLETE THIS METHOD
		CardNode first = deckRear.next;
		CardNode beforefirst = deckRear;
		while (first.cardValue != 27 && first.cardValue != 28){
			beforefirst = first;
			first = first.next;
		}
		CardNode second = first.next;
		while(second.cardValue != 27 && second.cardValue != 28){
			second = second.next;
		}
		if((deckRear.next.cardValue == 27 && deckRear.cardValue == 28) || (deckRear.cardValue == 27 && deckRear.next.cardValue == 28)){//if jokers are on both front and end
		}
		else if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28){//first joker is front, second joker becomes last card in modified deck
			deckRear = second;
		}
		else if(second == deckRear){//second joker is rear, first joker becomes first card in modified deck8
			deckRear = beforefirst;
		}
		else{//general case
			beforefirst.next = second.next;
			second.next = deckRear.next;
			deckRear.next = first;
			deckRear = beforefirst;
		}
	}
		//5 4 7 10 13 16 19 22 25 1 3 6 9 12 15 18 21 24 26 2 27 8 11 14 17 20 23 28
	
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
		void countCut() {// COMPLETE THIS METHOD
		CardNode prevRear = deckRear.next;
		while (prevRear.next != deckRear){
			prevRear = prevRear.next;
		}
		CardNode newfront = deckRear.next;
		CardNode newprevRear = deckRear;
		int amount = deckRear.cardValue;
		if (amount == 28 || amount == 27){
			amount = 0;
		}
		for (int i = amount; i != 0; i--){
			newfront = newfront.next;
			newprevRear = newprevRear.next;
		}
		if (deckRear.cardValue != 27 && deckRear.cardValue!= 28){
			prevRear.next = deckRear.next;
			newprevRear.next = deckRear;
			deckRear.next = newfront;
		}
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {// COMPLETE THIS METHOD
		jokerA();
		jokerB();
		tripleCut();
		countCut();
		int frontvalue = deckRear.next.cardValue;
		CardNode key = deckRear.next;
		if(frontvalue == 28) frontvalue = 27;
		for(int i = frontvalue; i!=0; i--){
			key = key.next;
		}
		if (key.cardValue == 27 || key.cardValue == 28){
			while(key.cardValue == 27 || key.cardValue == 28){
				jokerA();
				jokerB();
				tripleCut();
				countCut();
				key = deckRear.next;
				int frontvalue1 = deckRear.next.cardValue;
				if(frontvalue1 == 28) frontvalue1 = 27;
				for(int i = frontvalue1; i!=0; i--){
					key = key.next;
				}
			}
		}
		return key.cardValue;
	    //// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
		String edited = "";
		for (int i = 0; i<message.length(); i++){
			if(Character.isLetter(message.charAt(i))) edited = edited + message.charAt(i);
			edited = edited.toUpperCase();
		}
		String encrypt = "";
		for (int i = 0; i<edited.length(); i++){
			char letter = edited.charAt(i);
			int a = letter - 'A' + 1;
			int key = getKey();
			int total = a + key;
			if (total > 26) total = total - 26;
			char newletter = (char)(total-1+'A');
			encrypt = encrypt + newletter;
		}	
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    return encrypt;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
		String decrypt = "";
		for (int i = 0; i<message.length(); i++){
			char letter = message.charAt(i);
			int a = letter - 'A' + 1;
			int key = getKey();
			int total;
			if (a <= key){ 
				total = a + 26 - key;
			} else {
				total = a - key;
			}
			char newletter = (char)(total-1+'A');
			decrypt = decrypt + newletter;
		}	
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	    return decrypt;
	}
}
