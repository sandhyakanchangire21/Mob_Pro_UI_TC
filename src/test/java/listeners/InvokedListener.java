package listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class InvokedListener implements IInvokedMethodListener {
  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {}

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult result) {
    IRetryAnalyzer retry = result.getMethod().getRetryAnalyzer();
    if (retry == null) {
      return;
    }
    result.getTestContext().getFailedTests().removeResult(result.getMethod());
    result.getTestContext().getSkippedTests().removeResult(result.getMethod());
  }
}
