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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
specifier|public
class|class
name|MessageReplayObserver
implements|implements
name|MessageObserver
block|{
name|String
name|responseMessage
decl_stmt|;
specifier|public
name|MessageReplayObserver
parameter_list|(
name|String
name|responseMessage
parameter_list|)
block|{
name|this
operator|.
name|responseMessage
operator|=
name|responseMessage
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|in
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|read
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// do nothing
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|Conduit
name|backChannel
init|=
name|message
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|backChannel
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|InputStream
name|res
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|responseMessage
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|res
argument_list|,
name|out
argument_list|,
literal|2045
argument_list|)
expr_stmt|;
name|res
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|backChannel
operator|.
name|close
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

