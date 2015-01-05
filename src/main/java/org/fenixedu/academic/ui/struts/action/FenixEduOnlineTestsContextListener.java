package org.fenixedu.academic.ui.struts.action;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import org.fenixedu.academic.domain.onlineTests.Metadata;
import org.fenixedu.academic.domain.onlineTests.TestScope;
import org.fenixedu.academic.service.services.manager.MergeExecutionCourses;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduOnlineTestsContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MergeExecutionCourses.registerMergeHandler(FenixEduOnlineTestsContextListener::copyDistributedTestStuff);
        FenixFramework.getDomainModel().registerDeletionListener(ExecutionCourse.class, (executionCourse) -> {
            for (; !executionCourse.getMetadatasSet().isEmpty(); executionCourse.getMetadatasSet().iterator().next().delete()) {
                ;
            }
        });
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String uri = request.getRequestURI().substring(request.getContextPath().length());
                if ((uri.equals("/oldOnlineTests/testsManagement.do") || uri.equals("/student/studentTests.do"))
                        && "showImage".equals(request.getParameter("method"))) {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private static void copyDistributedTestStuff(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        for (final Metadata metadata : executionCourseFrom.getMetadatasSet()) {
            metadata.setExecutionCourse(executionCourseTo);
        }
        List<DistributedTest> distributedTests = TestScope.readDistributedTestsByTestScope(executionCourseFrom);
        for (final DistributedTest distributedTest : distributedTests) {
            final TestScope testScope = distributedTest.getTestScope();
            testScope.setExecutionCourse(executionCourseTo);
        }
    }
}
