begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_comment
comment|/**  * Log level of {@link LogRecord}. Based on SLF4J being popular facade for loggers like JUL, Log4J, JCL and so  * on. Severities order is: FATAL> ERROR> WARN> INFO> DEBUG> TRACE.  *<p>  * Mapping of levels:  *<ol>  *<li>JUL - same as<a href="http://www.slf4j.org/apidocs/org/slf4j/bridge/SLF4JBridgeHandler.html">SLF4J  * approach</a>.</li>  *<li>Log4J - levels are identical</li>  *</ol>  */
end_comment

begin_enum
annotation|@
name|XmlEnum
specifier|public
enum|enum
name|LogLevel
block|{
name|ALL
block|,
name|FATAL
block|,
name|ERROR
block|,
name|WARN
block|,
name|INFO
block|,
name|DEBUG
block|,
name|TRACE
block|,
name|OFF
block|;
annotation|@
name|XmlTransient
specifier|private
specifier|static
name|Map
argument_list|<
name|Level
argument_list|,
name|LogLevel
argument_list|>
name|fromJul
init|=
operator|new
name|HashMap
argument_list|<
name|Level
argument_list|,
name|LogLevel
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
specifier|private
specifier|static
name|Map
argument_list|<
name|LogLevel
argument_list|,
name|Level
argument_list|>
name|toJul
init|=
operator|new
name|HashMap
argument_list|<
name|LogLevel
argument_list|,
name|Level
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|ALL
argument_list|,
name|LogLevel
operator|.
name|ALL
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|LogLevel
operator|.
name|ERROR
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|LogLevel
operator|.
name|WARN
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
name|LogLevel
operator|.
name|INFO
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|LogLevel
operator|.
name|DEBUG
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|FINER
argument_list|,
name|LogLevel
operator|.
name|DEBUG
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
name|LogLevel
operator|.
name|TRACE
argument_list|)
expr_stmt|;
name|fromJul
operator|.
name|put
argument_list|(
name|Level
operator|.
name|OFF
argument_list|,
name|LogLevel
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|ALL
argument_list|,
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|FATAL
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|ERROR
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|WARN
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|INFO
argument_list|,
name|Level
operator|.
name|INFO
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|DEBUG
argument_list|,
name|Level
operator|.
name|FINE
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|TRACE
argument_list|,
name|Level
operator|.
name|FINEST
argument_list|)
expr_stmt|;
name|toJul
operator|.
name|put
argument_list|(
name|LogLevel
operator|.
name|OFF
argument_list|,
name|Level
operator|.
name|OFF
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates this enum from JUL {@link Level}.      */
specifier|public
specifier|static
name|LogLevel
name|fromJUL
parameter_list|(
name|Level
name|level
parameter_list|)
block|{
return|return
name|fromJul
operator|.
name|get
argument_list|(
name|level
argument_list|)
return|;
block|}
comment|/**      * Creates this JUL {@link Level} from this enum.      */
specifier|public
specifier|static
name|Level
name|toJUL
parameter_list|(
name|LogLevel
name|level
parameter_list|)
block|{
return|return
name|toJul
operator|.
name|get
argument_list|(
name|level
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

