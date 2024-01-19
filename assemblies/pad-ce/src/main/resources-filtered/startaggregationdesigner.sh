#!/bin/sh

# This program is free software; you can redistribute it and/or modify it under the
# terms of the GNU General Public License, version 2 as published by the Free Software
# Foundation.
#
# You should have received a copy of the GNU General Public License along with this
# program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html
# or from the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
# without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
#
# Copyright 2008 - ${copyright.year} Hitachi Vantara. All rights reserved.

# ${project.name}
# ${project.version}
# Copyright © 2008 - ${copyright.year} ${project.organization.name}
# Classpath is built by launcher. See ../lib/launcher.properties.

DIR=$( cd "$( dirname "$0" )" && pwd )

. "$DIR/set-pentaho-env.sh"
setPentahoEnv

"$_PENTAHO_JAVA" -jar "$DIR/lib/pentaho-application-launcher-${pentaho-launcher.version}.jar"
