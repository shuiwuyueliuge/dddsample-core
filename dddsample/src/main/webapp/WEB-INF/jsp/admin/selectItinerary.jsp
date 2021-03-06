<html>
<head>
  <title>Cargo Administration</title>
  <script type="text/javascript" charset="UTF-8" src="<c:url value="/js/calendar.js"/>"></script>
  <script type="text/javascript" charset="UTF-8" src="<c:url value="/js/YAHOO.js"/>"></script>
  <script type="text/javascript" charset="UTF-8" src="<c:url value="/js/event.js"/>"></script>
  <script type="text/javascript" charset="UTF-8" src="<c:url value="/js/dom.js"/>"></script>
  <style type="text/css" title="style" media="screen">
    @import "<c:url value="/calendar.css"/>";
  </style>
</head>
<body>
<div id="container">
  <form action="selectItinerary.html" method="GET">
  <table>
    <caption>Select route for cargo ${trackingId}</caption>
    <tr>
      <td><strong>Origin:</strong></td>
      <td>${origin}</td>
    </tr>
    <tr>
      <td><strong>Destination:</strong></td>
      <td>${destination}</td>
    </tr>
    <tr>
      <td><strong>Arrival deadline:</strong></td>
      <td>
        <input name="spec" type="text" size="10" id="cal1" value="${param.spec}"/>&nbsp;
        <img alt="" src="<c:url value="/images/calendarTrigger.gif"/>" class="calendarTrigger" onclick="calendar.toggle( event, this, 'cal1')"/>
      </td>
    </tr>
  </table>
    <p>
      <input type="hidden" name="trackingId" value="${trackingId}"/>
      <input type="submit" value="Find routes"/>
    </p>
  </form>
  <c:url value="/admin/assignItinerary.html" var="postUrl"/>

  <c:forEach items="${itineraryCandidates}" var="it" varStatus="itStatus">
      <form action="${postUrl}" method="post">
        <input type="hidden" name="trackingId" value="${trackingId}"/>
        <table>
          <caption>Route ${itStatus.index + 1}</caption>
          <thead>
            <tr>
              <td>Carrier Movement</td>
              <td>From</td>
              <td>To</td>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${it.legs}" var="leg" varStatus="legStatus">
              <input type="hidden" name="legs[${legStatus.index}].carrierMovementId" value="${leg.carrierMovementId}"/>
              <input type="hidden" name="legs[${legStatus.index}].fromUnLocode" value="${leg.from}"/>
              <input type="hidden" name="legs[${legStatus.index}].toUnLocode" value="${leg.to}"/>
              <tr>
                <td>${leg.carrierMovementId}</td>
                <td>${leg.from}</td>
                <td>${leg.to}</td>
              </tr>
            </c:forEach>
          </tbody>
          <tfoot>
            <tr>
              <td colspan="3">
                <p>
                  <input type="submit" value="Assign cargo to this route"/>
                </p>
              </td>
            </tr>
          </tfoot>
        </table>
      </form>
  </c:forEach>

</div>
</body>
</html>