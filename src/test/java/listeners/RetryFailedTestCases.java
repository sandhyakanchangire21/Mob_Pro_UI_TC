package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTestCases implements IRetryAnalyzer {
  private int retryCnt = 0;
  // We could mentioned maxRetryCnt (Maximum Retry Count) as per our requirement. Here I took 1, If
  // any failed test cases then it runs one times
  private static final int maxRetryCnt = 1;

  // This method will be called every time a test fails. It will return TRUE if a test fails and
  // need to be retried, else it returns FALSE
  public boolean retry(ITestResult result) {
    if (retryCnt < maxRetryCnt) {
      System.out.println(
          "Retrying " + result.getName() + " again and the count is " + (retryCnt + 1));
      retryCnt++;
      return true;
    }
    return false;
  }
}
