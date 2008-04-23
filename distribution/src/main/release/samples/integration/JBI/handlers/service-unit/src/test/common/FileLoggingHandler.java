begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|test
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPMessageContext
import|;
end_import

begin_comment
comment|/*  * This simple SOAPHandler will output the contents of incoming  * and outgoing messages into a file.  */
end_comment

begin_class
specifier|public
class|class
name|FileLoggingHandler
extends|extends
name|LoggingHandler
block|{
specifier|public
name|FileLoggingHandler
parameter_list|()
block|{
try|try
block|{
name|setLogStream
argument_list|(
operator|new
name|PrintStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
literal|"demo.log"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Could not open log file demo.log"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|init
parameter_list|(
name|Map
name|c
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FileLoggingHandler : init() Called...."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|smc
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FileLoggingHandler : handleMessage Called...."
argument_list|)
expr_stmt|;
name|logToSystemOut
argument_list|(
name|smc
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|SOAPMessageContext
name|smc
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FileLoggingHandler : handleFault Called...."
argument_list|)
expr_stmt|;
name|logToSystemOut
argument_list|(
name|smc
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

