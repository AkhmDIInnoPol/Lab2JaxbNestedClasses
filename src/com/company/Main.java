package com.company;

import com.company.interactor.BackUpCheckJournal;
import com.company.interactor.BackUpCheckMark;
import com.company.interactor.BackUpCheckTaskToGroup;


public class Main {

    public static void main(String[] args) {
	// write your code here

        BackUpCheckJournal backUpCheckJournalThread = new BackUpCheckJournal();
        backUpCheckJournalThread.start();

        BackUpCheckMark backUpCheckMarkThread = new BackUpCheckMark();
        backUpCheckMarkThread.start();

        BackUpCheckTaskToGroup backUpCheckTaskToGroupThread = new BackUpCheckTaskToGroup();
        backUpCheckTaskToGroupThread.start();
    }
}
