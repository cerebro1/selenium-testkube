import os
import pytest
import logging
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

# Configure logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(message)s")
results_dir = "/data/test-results"
os.makedirs(results_dir, exist_ok=True)

@pytest.fixture(scope="session")
def driver():
	hub_url = os.getenv('SELENIUM_HUB_URL', 'http://selenium-grid-testkube-selenium-hub.testkube:4444/wd/hub')
	logging.info(f"Connecting to Selenium Grid Hub at: {hub_url}")
	options = webdriver.ChromeOptions()
	driver = webdriver.Remote(command_executor=hub_url, options=options)
	yield driver
	driver.quit()

def test_google_search(driver):
	logging.info("Navigating to Google.com")
	driver.get("https://www.google.com")
	search_box = driver.find_element(By.NAME, "q")
	search_box.send_keys("Testkube Selenium Grid")
	search_box.send_keys(Keys.RETURN)
	time.sleep(3)

	assert "Testkube" in driver.page_source

	# Capture and save a screenshot
	screenshot_path = os.path.join(results_dir, "google_screenshot.png")
	driver.save_screenshot(screenshot_path)
	logging.info(f"Screenshot saved at {screenshot_path}")

	# Save test output to a file
	output_path = os.path.join(results_dir, "test_output.txt")
	with open(output_path, "w") as f:
		f.write(driver.page_source)

	logging.info(f"Test output saved to {output_path}")
