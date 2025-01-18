package todolist;

import todolist.Todo;


final class Node {
	

	Todo todo;
	Node next;


	Node(Todo todo, Node next) {
		this.todo = todo;
		this.next = next;
	}


	Node(Todo todo) {
		this(todo, null);
	}

} 
