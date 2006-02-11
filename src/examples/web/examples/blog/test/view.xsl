<!--
    Copyright (C) 2005 Orbeon, Inc.

    This program is free software; you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation; either version
    2.1 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
    without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Lesser General Public License for more details.

    The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
-->
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:xforms="http://www.w3.org/2002/xforms"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xsl:version="2.0">
    <head>
        <title>Test Calculate</title>
    </head>
    <body>
        <xforms:group>
            <p>
                Value 1: <xforms:output ref="/form/value1"/>
                <br/>
                Value 2: <xforms:output ref="/form/value2"/>
                <br/>
                Value 3: <xforms:output ref="/form/value3"/>
            </p>
        </xforms:group>
    </body>
</html>