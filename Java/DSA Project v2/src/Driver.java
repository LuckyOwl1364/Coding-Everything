import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
	/** Reader used to receive input **/
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	// Caitlin McElwee
	public static void main(String[] args) throws NumberFormatException, IOException {
		System.out.println("Welcome to the Shopping Center!!!");

		// List ADT which stores items in the store's stock
		ListArrayBasedPlus items = new ListArrayBasedPlus();
		int restock = loadStock(items);
		int lineTurn = chooseStartingLine();
		printMenu(items, restock, lineTurn);
	} // end main

	/**
	 * Loads the item stock for the store and sets the restocking level of items.
	 * 
	 * @param items a list of items in stock
	 * @return an integer value used to set the benchmark for the restocking level of items
	 * @throws IOException
	 */
	// Caitlin McElwee
	private static int loadStock(ListArrayBasedPlus items) throws IOException {
		System.out.println("Please specify stock.");

		System.out.printf(" How many items do you have?");
		int itemCount = Integer.parseInt(br.readLine().trim()); // # of discrete products in stock
		System.out.println(itemCount);

		System.out.printf("Please specify restocking amount:");
		int restock = Integer.parseInt(br.readLine().trim()); // # of discrete products in stock
		System.out.println(restock);

		for (int i = 0; i < itemCount; i++) {
			System.out.printf(">>Enter item name : ");
			String itemName = br.readLine().trim();
			System.out.println(itemName);

			System.out.printf("How many %ss? ", itemName);
			int inStock = Integer.parseInt(br.readLine().trim()); // # of this
																	// product
																	// in stock
			System.out.println(inStock);

			Item item = new Item(itemName, inStock); // create new item
			if (items.size() > 0) {
				int index = searchBinary(itemName, items);
				if (index < 0) {
					items.add(-1 * (index) - 1, item);
					System.out.printf("%s items of %s have been placed in stock.\n", inStock, itemName);
				} else {
					System.out.println("Item already in stock!");
				}
			} else {
				items.add(0, item);
				System.out.printf("%s items of %s have been placed in stock.\n", inStock, itemName);
			}
		} // end for
		return restock;
	} // end loadStock

	/**
	 * Determines which checkout line can checkout a Customer first.
	 * 
	 * @return integer value indicating which checkout line checks out first
	 * @throws IOException
	 */
	// Tom Harker
	private static int chooseStartingLine() throws IOException {
		int lineTurn;

		System.out.println(
				"Please select the checkout line that should check out customers first (regular1/regular2/express): ");
		String input = br.readLine().trim().toLowerCase();
		System.out.println(input);
		switch (input) {
		case "regular1":
			lineTurn = 0;
			break;
		case "regular2":
			lineTurn = 1;
			break;
		case "express":
			lineTurn = 2;
			break;
		default:
			lineTurn = 0;
			break;
		} // end switch
		return lineTurn;
	}
	
	/**
	 * Prints the menu so the user can input commands.
	 * 
	 * @param items a list containing the items in stock
	 * @param restock integer determining the restock level for items
	 * @param lineTurn integer determining which checkout line will checkout next
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	// Caitlin McElwee
	private static void printMenu(ListArrayBasedPlus items, int restock, int lineTurn)
			throws NumberFormatException, IOException {
		boolean finished = false;

		int storeTime = 0;

		// List ADT which stores customers currently shopping
		ListArrayBasedPlus shopCustomers = new ListArrayBasedPlus();

		// An issue with using Queue ADT is I don't think you can directly
		// access size of collection
		// Using local variables to track the size of each queue
		ListArrayBasedPlus regular1 = new ListArrayBasedPlus();
		ListArrayBasedPlus regular2 = new ListArrayBasedPlus();
		ListArrayBasedPlus express = new ListArrayBasedPlus();

		// How long the shop has been open
		storeTime = 0;

		do {
			System.out.print("\nSelect from the following menu:" + "\n\t1. Customer enters Shopping Center."
					+ "\n\t2. Customer picks an item and places it the shopping cart."
					+ "\n\t3. Customer removes an item from the shopping cart." + "\n\t4. Customer is done shopping."
					+ "\n\t5. Customer checks out." + "\n\t6.  Print info about customers who are shopping."
					+ "\n\t7.  Print info about customers in checkout lines."
					+ "\n\t8.  Print info about items at or below re-stocking level." + "\n\t9.  Reorder an item."
					+ "\n\t10. Close the Shopping Center." + "\nMake you menu selection now: ");

			// Prevent NumberFormatException from crashing program
			int select;
			try {
				select = Integer.parseInt(br.readLine().trim());
			} catch (NumberFormatException e) {
				select = -1;
			}

			System.out.print(select + "\n\n");

			switch (select) {
			case 1:
				addCustomer(shopCustomers, regular1, regular2, express, storeTime);
				break;
			case 2:
				storeTime += useStock(shopCustomers, items, regular1, regular2, express, true);
				break;
			case 3:
				storeTime += useStock(shopCustomers, null, regular1, regular2, express, false);
				break;
			case 4:
				doneShopping(shopCustomers, express, regular1, regular2, storeTime);
				break;
			case 5:
				lineTurn = checkout(shopCustomers, express, regular1, regular2, lineTurn, storeTime);
				break;
			case 6:
				printShoppers(shopCustomers, storeTime);
				break;
			case 7:
				printCheckoutLines(express, regular1, regular2);
				break;
			case 8:
				printRestock(items, restock);
				break;
			case 9:
				reorder(items);
				break;
			case 10:
				finished = true;
				break;
			default:
				System.out.println("Error, please use valid input");
				break;
			} // end switch

		} while (!finished); // end do-while

		System.out.println("\tExiting program...Good Bye");
		System.exit(0);

	} // end printMenu

	/**
	 * A binary search which, if successful, returns a positive number with the index of found item.
	 * If unsuccessful, returns a negative number indicating where this item would be inserted. index = (-(index+1)).
	 * 
	 * @param item the search string 
	 * @param list a sorted list of NamedObjects
	 * @return
	 */
	// Tom Harker
	// Tom Harker
	public static int searchBinary(String item, ListArrayBasedPlus list) {
		// TODO Auto-generated method stub
		int numItems = list.size();
		int low = 0;
		int high = numItems == 0 ? 0 : numItems - 1;
		int mid = (high + low) / 2;

		if (numItems > 0) {
			// Binary search for index
			// Loop until high == low
			while (high > low) {
				if (item.compareTo(((NamedObject) list.get(mid)).getName()) <= 0) {
					high = mid;
				} else {
					low = mid + 1;
				}
				mid = (high + low) / 2;
			}

			// Check if search item is found at this index
			if (item.compareTo(((NamedObject) list.get(mid)).getName()) == 0)
				return mid;

			// If not found, check if search item should go at or after this
			// index
			// Return negative number to indicate search item not found
			// Return value shifted by one so -1 is first index, -2 is second,
			// etc.
			else if (item.compareTo(((NamedObject) list.get(++mid - 1)).getName()) < 0) {
				mid = 0 - mid;
			} else {
				mid = 0 - ++mid;
			}
		}
		return mid;
	}

	// #1
	/**
	 * Adds a customer to the shoppingCustomers list. 
	 * If a Customer with the given name is already in the store, prompts for another name until a valid one is provided.
	 * 
	 * @param shopCustomers sorted list of shopping Customers
	 * @param regular1 sorted list of Customers in first checkout line
	 * @param regular2 sorted list of Customers in second checkout line
	 * @param express sorted list of Customers in express checkout
	 * @param storeTime the amount of time the store has been open
	 * @throws IOException
	 */
	// Tom Harker
	// Tom Harker
	private static void addCustomer(ListArrayBasedPlus shopCustomers, ListArrayBasedPlus regular1, 
			ListArrayBasedPlus regular2, ListArrayBasedPlus express, int storeTime) throws IOException {
		boolean complete = false;
		while(!complete) {
			System.out.print("Enter customer name: ");
			String name = br.readLine().trim();
			System.out.println(name);
			boolean found = false;
			if(express.size() > 0 && searchBinary(name, express) >= 0) 
				found = true;
			if(!found && regular1.size() > 0 && searchBinary(name, regular1) >= 0)
				found = true;
			if(!found && regular2.size() > 0 && searchBinary(name, regular2) >= 0)
				found = true;
			if(!found) {
				int index = searchBinary(name, shopCustomers);
				if (shopCustomers.size() > 0) {
					if (index < 0) {
						shopCustomers.add(-1 * (index) - 1, new Customer(storeTime, name));
						System.out.println("Customer " + name + " is now in the Shopping Center.");
						complete = true;
					} else {
					System.out.println("Customer " + name + " is already in the Shopping Center!");
					}
				} else {
					shopCustomers.add(0, new Customer(storeTime, name));
					System.out.println("Customer " + name + " is now in the Shopping Center.");
					complete = true;
				}
			}
			else {
				System.out.printf("Customer %s is in a checkout line! \n", name);
			}
		}
	}

	// #2,3
	/**
	 * Adds or removes an item from a Customers cart.
	 * If a Customer with the given name is not in the store or is in the checkout line, 
	 * prompts for another name until a valid one is provided.
	 * 
	 * @param shopCustomers a list of Customers currently shopping
	 * @param items a list of items in stock
	 * @param regular1 a list containing Customers in checkout line 1
	 * @param regular2 a list containing Customers in checkout line 2
	 * @param express a list containing Customers in express checkout
	 * @param taking boolean flag indicating whether or not a Customer is picking up or removing an item from the cart.
	 * @return integer indicating if a Customer completed an operation or not
	 * @throws IOException
	 */
	// Caitlin McElwee
	private static int useStock(ListArrayBasedPlus shopCustomers, ListArrayBasedPlus items,
			ListArrayBasedPlus regular1, ListArrayBasedPlus regular2, ListArrayBasedPlus express, boolean taking)
			throws IOException {
		int numAdded = 0;
		if (shopCustomers.isEmpty()) { // if no one in shopping center
			System.out.println("No one is in the Shopping Center!");
		} // end if (shopCustomers.isEmpty())
		else {
			boolean complete = false;
			while(!complete) {
				System.out.printf(">>Enter customer name : ");
				String custName = br.readLine().trim();
				System.out.println(custName);
				boolean found = false;
				if(express.size() > 0 && searchBinary(custName, express) > 0) 
					found = true;
				if(!found && regular1.size() > 0 && searchBinary(custName, regular1) > 0)
					found = true;
				if(!found && regular2.size() > 0 && searchBinary(custName, regular2) > 0)
					found = true;
				if(!found) {
					int custIndex = searchBinary(custName, shopCustomers);
				
					if (custIndex >= 0) { // check if such a customer exists
						Customer foundCust = (Customer) shopCustomers.get(custIndex);
						complete = true;
						if (taking) { // if customer is taking an item from stock
							System.out.printf(">>What item does %s want? ", custName);
							String itemName = br.readLine().trim();
							System.out.println(itemName);
							int itemIndex = searchBinary(itemName, items);
							if (itemIndex >= 0) { // check if such an item exists
								Item item = (Item) items.get(itemIndex);
								if (item.getCount() > 0) { // if count > 0
									foundCust.setNumItems(foundCust.getNumItems() + 1); // add 1 to the customer's numItems
									item.decrementStock(); // and decrement item count
									numAdded = 1;
									System.out.printf("Customer %s now has %s items in the shopping cart.\n", custName,
											foundCust.getNumItems());
								} else {
									System.out.printf("There are no %ss in stock. \n", itemName);
								}
							} // end if (search(items,itemName) != null)
							else {
								System.out.printf("No %ss in Shopping Center.\n", itemName); // declare there is no such item
							} // end else (search(items,itemName) != null)
						} // end if (taking)
						else { // customer is removing an item from their cart (!taking)
							foundCust.setNumItems(foundCust.getNumItems() - 1); // subtract 1 from the customer's numItems
							System.out.printf("Customer %s now has %s items in the shopping cart\n", custName,
							foundCust.getNumItems());
							numAdded = 1;
						} // end else (taking)
					} // end if
					else {
						// declare there is no such customer
						System.out.printf("No %ss in Shopping Center.\n", custName);
					} // end else (foundCust != null))
			}
			else {
				System.out.printf("Customer %s is in a checkout line!", custName);
			}
		}
		} // end else
		return numAdded;
	} // end takeItem
	
	// #4
	/**
	 * Moves the customer who has been shopping the longest to one of the checkout lines.
	 * 
	 * @param shopCustomers sorted list of Customers shopping
	 * @param express sorted list of Customers in express checkout
	 * @param regular1 sorted list of Customers in the first checkout
	 * @param regular2 sorted list of Customers in the second checkout
	 * @param storeTime the amount of time the store has been open
	 * @throws IOException
	 */
	// Tom Harker
	private static void doneShopping(ListArrayBasedPlus shopCustomers, ListArrayBasedPlus express,
			ListArrayBasedPlus regular1, ListArrayBasedPlus regular2,
			int storeTime) throws IOException {
		if (shopCustomers.size() > 0) {
			int longestWaiting = findLongestWaiting(shopCustomers);
			Customer c = (Customer) shopCustomers.get(longestWaiting);
			shopCustomers.remove(longestWaiting);

			if (c.getNumItems() != 0) {

				// Local var used to print what line customer went to
				String line;
				int checkoutIndex;
				int exp = express.size();
				int num1 = regular1.size();
				int num2 = regular2.size();

				// If less than 5 items, can potentially go to express.
				ListArrayBasedPlus searchLine;
				if (c.getNumItems() < 5) {
					if (exp == 0 || ((exp <= 2*num1 || exp <= 2*num2) && num1 != 0 && num2 != 0)) {
						searchLine = express;
						line = "express";
					} else if (num2 < num1) {
						searchLine = regular2;
						line = "second checkout";
					} else {
						searchLine = regular1;
						line = "first checkout";
					}
				}
				// If more than 5 items can only go to checkout lines 1 or 2.
				else {
					if (num2 < num1) {
						searchLine = regular2;
						line = "second checkout";
					} else {
						searchLine = regular1;
						line = "first checkout";
					}
				}

				// Add customer to the checkout line determined above
				if(searchLine.size() == 0) {
					searchLine.add(0, c);
				}
				else {
					checkoutIndex = searchBinary(c.getName(), searchLine);
					if(checkoutIndex < 0) {
						searchLine.add(-1*(checkoutIndex)-1, c);
					}
				}

				System.out.println(
						"After " + (storeTime - c.getTimeInStore()) + " minutes in the Shopping Center" + " customer "
								+ c.getName() + " with " + c.getNumItems() + " items is now in the " + line + " line.");
			}
			// If customer has 0 items, ask if they want to leave or keep shopping
			else {
				System.out
				.print("Should customer " + c.getName() + " checkout or continue shopping? Checkout? (Y/N): ");
				String input = br.readLine().trim().toUpperCase();
				System.out.println(input);
				if (input.equals("N")) {
					c.setTimeInStore(storeTime);
					int index = searchBinary(c.getName(), shopCustomers);
					if(index < 0) {
						shopCustomers.add(-1*(index)-1, c);
						System.out.println("Customer " + c.getName() + " with " + c.getNumItems() + " items returned to shopping.");
					}
					else {
						System.out.println("Customer with name " + c.getName() + " already in store.");
					}
				} else if (input.equals("Y")) {
					System.out.println("Customer " + c.getName() + " with " + c.getNumItems() + " checked out.");
				}
			}
		} else {
			System.out.println("No customers in the Shopping Center!");
		}
	}

	// #5
	/**
	 * Turn-based checkout system.
	 * If no Customers are in line, no checkouts occur.
	 * If one or more Customers are in line, the Customer of the selected checkout line who has waited longest checks out. 
	 * If no Customers are in this line, the next line is selected repetitively until a Customer can check out. 
	 * 
	 * @param shopCustomers sorted list of Customers who are shopping
	 * @param express sorted list of Customers in express checkout
	 * @param regular1 sorted list of Customers in first checkout line
	 * @param regular2 sorted list of Customers in second checkout line
	 * @param lineTurn integer indicating which checkout line will checkout next
	 * @param storeTime amount of time the store has been open
	 * @return integer value indicating which checkout line is next to checkout
	 * @throws IOException
	 */
	// Tom Harker
	private static int checkout(ListArrayBasedPlus shopCustomers, ListArrayBasedPlus express,
			ListArrayBasedPlus regular1, ListArrayBasedPlus regular2,
			int lineTurn, int storeTime) throws IOException {
		// If the checkout lines are empty
		int num1 = regular1.size();
		int num2 = regular2.size();
		int exp = express.size();
		if (num1 == 0 && num2 == 0 && exp == 0) {
			System.out.println("There are no customers in any lines!");
		} else {
			// I hate this placeholder variable but haven't found a nice way to
			// remove it yet
			Customer c = new Customer(0, "Placeholder variable");
			// Flag for when someone has checked out
			boolean checkout = false;
			int checkoutIndex;
			// Check lines in turn order while checking if a checkout has occurred
			if (lineTurn == 0 && !checkout) {
				// If they have at least one person in line, check out
				if (num1 > 0) {
					checkoutIndex = findLongestWaiting(regular1);
					c = (Customer)regular1.get(checkoutIndex);
					regular1.remove(checkoutIndex);
					checkout = true;
				}
				// Advance to next line
				lineTurn = (lineTurn + 1) % 3;
			}
			if (lineTurn == 1 && !checkout) {
				// If they have at least one person in line, check out
				if (num2 > 0) {
					checkoutIndex = findLongestWaiting(regular2);
					c = (Customer)regular2.get(checkoutIndex);
					regular2.remove(checkoutIndex);
					checkout = true;
				}
				// Advance to next line
				lineTurn = (lineTurn + 1) % 3;
			}
			if (lineTurn == 2 && !checkout) {
				// If they have at least one person in line, check out
				if (exp > 0) {
					checkoutIndex = findLongestWaiting(express);
					c = (Customer)express.get(checkoutIndex);
					express.remove(checkoutIndex);
					checkout = true;
				}
				// Advance to next line
				lineTurn = (lineTurn + 1) % 3;
			}

			// Have to include these for the case when it's line 1's turn but
			// both line 1 and 2 have zero people in line
			// Or when it's line 2's turn but line 2 and line 0 have zero people
			// in line
			// These two blocks "wrap back around" the loop to catch these
			// special cases
			if (lineTurn == 0 && !checkout) {
				if (num1 > 0) {
					checkoutIndex = findLongestWaiting(regular1);
					c = (Customer)regular1.get(checkoutIndex);
					regular1.remove(checkoutIndex);
					checkout = true;
				}
				lineTurn = (lineTurn + 1) % 3;
			}
			if (lineTurn == 1 && !checkout) {
				if (num2 > 0) {
					checkoutIndex = findLongestWaiting(regular2);
					c = (Customer)regular2.get(checkoutIndex);
					regular2.remove(checkoutIndex);
					checkout = true;
				}
				lineTurn = (lineTurn + 1) % 3;
			}

			System.out.print("Should customer " + c.getName() + " leave or keep on shopping? Leave? (Y/N): ");
			String in = br.readLine().trim().trim();
			System.out.println(in);
			if (in.equals("N")) {
				c.setTimeInStore(storeTime);
				int index = searchBinary(c.getName(), shopCustomers);
				if(index < 0) {
					shopCustomers.add(-1*(index)-1, c);
					System.out.println("Customer " + c.getName() + " with " + c.getNumItems() + " items returned to shopping.");
				}
			} else if (in.equals("Y")) {
				System.out.println("Customer " + c.getName() + " is now leaving the Shopping Center.");
			}
		}
		return lineTurn;
	}

	/**
	 * Searches for the Customer in the shopCustomers list who has been in the store the longest.
	 * The index of this entry is returned. 
	 * 
	 * @param shopCustomers sorted list of Customers.
	 * @return index of Customer waiting the longest
	 */
	// Tom Harker
	private static int findLongestWaiting(ListArrayBasedPlus shopCustomers) {
		int longestWaiting = 0;
		for (int i = 1; i < shopCustomers.size(); i++) {
			if (((Customer) shopCustomers.get(longestWaiting)).getTimeInStore() >= ((Customer) shopCustomers.get(i))
					.getTimeInStore()) {
				longestWaiting = i;
			}
		}
		return longestWaiting;
	}

	// #6
	/**
	 * Prints each Customer in the shopCustomers list
	 * For each customer, the number of items they are holding and the time they have spent in store are printed.
	 * 
	 * @param shopCustomers a list of Customers currently shopping
	 * @param storeTime the amount of time the store has been open
	 */
	// Tom Harker
	private static void printShoppers(ListArrayBasedPlus shopCustomers, int storeTime) {
		if(shopCustomers.size() > 0) {
			System.out.println("The following " + shopCustomers.size() + " customers are in the Shopping Center: ");
			for (int i = 0; i < shopCustomers.size(); i++) {
				Customer c = (Customer) shopCustomers.get(i);
				System.out.println("Customer " + c.getName() + " with " + c.getNumItems() + " items present for "
						+ (storeTime - c.getTimeInStore()) + " minutes.");
			}
		}
		else {
			System.out.println("There are no customers in the Shopping Center!");
		}
	}

	// #7
	/**
	 * Prints the contents of each checkout line.
	 * 
	 * @param express sorted list containing Customers in express checkout
	 * @param regular1 sorted list containing Customers in first checkout line
	 * @param regular2 sorted list containing Customers in second checkout line
	 */
	// Tom Harker
	private static void printCheckoutLines(ListArrayBasedPlus express, ListArrayBasedPlus regular1,
			ListArrayBasedPlus regular2) {
		if (regular1.size() == 0) {
			System.out.println("There are no customers in checkout line 1!");
		} else {
			System.out.println("The following customer(s) are in checkout line 1: ");
			System.out.println(regular1.toString());
		}
		if (regular2.size() == 0) {
			System.out.println("There are no customers in checkout line 2!");
		} else {
			System.out.println("The following customer(s) are in checkout line 2: ");
			System.out.println(regular2.toString());
		}
		if (express.size() == 0) {
			System.out.println("There are no customers in the express line!");
		} else {
			System.out.println("The following customer(s) are in the express line:");
			System.out.println(express.toString());
		}
	}
	
	// #8
	/**
	 * Prints all items in the item list which are at or below restocking level.
	 * 
	 * @param items sorted list of items in the store
	 * @param restock indicates the restocking level for items
	 */
	// Caitlin McElwee
	private static void printRestock(ListArrayBasedPlus items, int restock) {
		int size = items.size();

		System.out.println("Items at re-stocking level:");

		for (int i = 0; i < size; i++) {
			Item currentItem = (Item) items.get(i);

			if (currentItem.getCount() <= restock) {
				System.out.printf("%s with %s items.\n", currentItem.getName(), currentItem.getCount());
			} // end if (currentItem.getCount() <= restock)

		} // end for
	} // end printRestock

	// #9
	/**
	 * Orders more stock for items in the item list.
	 * 
	 * @param items sorted list of items in the store
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	// Caitlin McElwee
	private static void reorder(ListArrayBasedPlus items) throws NumberFormatException, IOException {
		System.out.printf(">>Enter item name to be re-ordered : ");
		String itemName = br.readLine().trim();
		System.out.println(itemName);

		int itemIndex = searchBinary(itemName, items);

		if (itemIndex >= 0) { // check if such an item exists
			System.out.printf("Enter number of %ss to be re-ordered : ", itemName);
			int numReordered = Integer.parseInt(br.readLine().trim()); // # of this product to reorder
			System.out.println(numReordered);

			System.out.printf("Stock now has %s %ss.\n", ((Item) items.get(itemIndex)).addStock(numReordered),
					itemName);
		} // end if (search(items,itemName) != null)
		else {
			System.out.printf("%s is not in stock!\n", itemName); // declare there is no such item
		} // end else (search(items,itemName) != null)
	} // end reorder
} // end class
