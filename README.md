# Mountain
<h2>Mountain represents a society management website. Frontend is based on JSP and backend is implemented via Spring Boot which was connected to MySQL database. </h2>

<h3>Things you can do:</h3>

<ul style="list-style-type:disc;">
  <li>Register/Login
  <br>
  - Based on 3 types of roles: admin(sekretar), user(clan), guest(gost) authanticated by Spring Security
  <br>
  <li>See available mountains/hotels/attractions as well as available slots in each hotel</li>
  - Logged user represents the memeber of society
  <br> 
  <li>Book a room/hike </li>
  - Every memeber has permission to book a room in every hotel that has available slots. Also on every mountain there are all sorts of tourist attractions which can also be visited by them.
  <li>Post images and reports</li>
  - User can post images and reports from his last visit to one of the mountains
  <li>Comment attractions</li>
  - User can comments attractions if he visited them in the past.
  <li>Modify user</li>
  - Admin has the permission to modify every info about user.
  <li>Generate report<li>
  - Admin has the permission to generate booking report which includes various sort of informations about mountains,hotels and attractions.
</ul>

<h3>Techniques used while implementing application:</h3>

<ul style="list-style-type:disc;">
  <li>Creating and MySQL database</li>
  <li>Manipulating data gathered from database model</li>
  <li>Switching user from page to page via REST calls</li>
  <li>Displaying data using JSP</li>
</ul>
