<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/includeMathJax.jsp" />


<div class="row col-lg-12">
	<ol class="breadcrumb">
			<em>${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> ${degree.sigla} </c:forEach>)
			</em>
		</ol>
</div>
<span class="row col-lg-12">
	<div style="display:inline-block;">
		<div class="dropdown">
		  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownTests" data-toggle="dropdown" aria-expanded="true">
		    <bean:message key="title.tests"/> <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownTests">
				<li role="presentation">
					<html:link  tabindex="-1" page="/testsManagement.do?method=prepareCreateTest" paramId="executionCourseID" paramName="executionCourseID">
						<bean:message key="link.createTest"/>
					</html:link>
				</li>
				<li role="presentation">
					<html:link tabindex="-1" page="/testsManagement.do?method=showTests" paramId="executionCourseID" paramName="executionCourseID">
						<bean:message key="link.showTests"/>
					</html:link>
				</li>
				<li role="presentation">
					<html:link  tabindex="-1" page="/testsManagement.do?method=showDistributedTests" paramId="executionCourseID" paramName="executionCourseID">
						<bean:message key="link.showDistributedTests"/>
					</html:link>
				</li>
			</ul>
		</div>
	</div>
	<div style="display:inline-block;">
		<div class="dropdown">
		  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownExercices" data-toggle="dropdown" aria-expanded="true">
		    <bean:message key="title.exercises"/> <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownExercices">
				<li>
                               <html:link page="/exercisesManagement.do?method=exercisesFirstPage" paramId="executionCourseID" paramName="executionCourseID">
                                       <bean:message key="link.showExercises"/>
                               </html:link>
                       </li>
                       <li>
                               <html:link page="/exercisesManagement.do?method=chooseNewExercise" paramId="executionCourseID" paramName="executionCourseID">
                                       <bean:message key="link.createExercise"/>
                               </html:link>
                       </li>
                       <li>
                               <html:link page="/exercisesManagement.do?method=insertNewExercise" paramId="executionCourseID" paramName="executionCourseID">
                                       <bean:message key="link.importExercise"/>
                               </html:link>
                       </li>
			</ul>
		</div>
	</div>
</span>




<div class="row">
	
<!-- 	<nav class="col-lg-2" id="context"> -->
<!-- 		<ul class="nav nav-pills nav-stacked"> -->
<!-- 			<li> -->
<%-- 				<html:link page="/manageExecutionCourse.do?method=instructions" paramId="executionCourseID" paramName="executionCourseID"> --%>
<%-- 					<bean:message key="label.back"/> --%>
<%-- 				</html:link> --%>
<!-- 			</li> -->
<!-- 			<li> -->
<%-- 				<html:link page="/testsManagement.do?method=testsFirstPage" paramId="executionCourseID" paramName="executionCourseID"> --%>
<%-- 					<bean:message key="link.home"/> --%>
<%-- 				</html:link> --%>
<!-- 			</li> -->
<!-- 		</ul> -->
<!-- 	</nav> -->
	<main class="col-lg-12">
		<jsp:include page="${teacher$actual$page}" />
	</main>
</div>

<style>

.gen-button {
	margin: 0;
	padding: 0 5px 0;
}

</style>