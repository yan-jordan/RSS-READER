# RSS Reader

## Overview

This project is a simple RSS Reader implemented in Java. It allows users to manage a list of RSS feed URLs, add new ones, remove existing ones, and fetch the latest updates from the subscribed feeds.

## Features

- **Show Updates**: Display the latest updates from all or selected RSS feeds.
- **Add URL**: Add a new RSS feed URL to the list.
- **Remove URL**: Remove an existing RSS feed URL from the list.
- **Persistent Storage**: Store and retrieve URLs from a file (`data.txt`).

## Requirements

- Java Development Kit (JDK) 8 or later
- JSoup library for parsing HTML
- An internet connection for fetching RSS feeds
- 
## Code Explanation

### Class: `RssReader`

- **Constructor**: Reads URLs from `data.txt` and welcomes the user.
- **Methods**:
  - `welcoming()`: Displays the main menu and handles user input.
  - `addURL()`: Adds a new RSS feed URL to the list and saves it to `data.txt`.
  - `showUpdate()`: Shows updates from the RSS feeds.
  - `removeURL()`: Removes a URL from the list and updates `data.txt`.
  - `extractPageTitle(String html)`: Extracts the page title from a given HTML.
  - `retrieveRssContent(String rssUrl)`: Fetches and displays the content of an RSS feed.
  - `fetchPageSource(String urlString)`: Fetches the page source of a given URL.
  - `extractRssUrl(String url)`: Extracts the RSS feed URL from a given page.

### Main Method

- The `main` method creates an instance of `RssReader` and calls the `welcoming()` method to start the program.

## Data Storage

- **File**: `data.txt`
- **Format**: Each line contains the page title, URL, and RSS feed URL separated by semicolons.

## Example

To add a new RSS feed URL:
1. Select option `[2] ADD URL` from the main menu.
2. Enter the URL of the website.
3. The program will extract the page title and RSS feed URL and add them to the list.

To display updates:
1. Select option `[1] SHOW UPDATES`.
2. Choose to display updates from all websites or a specific one.
