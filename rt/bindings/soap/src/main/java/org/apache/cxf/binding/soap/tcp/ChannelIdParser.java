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
name|binding
operator|.
name|soap
operator|.
name|tcp
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ChannelIdParser
block|{
specifier|private
name|ChannelIdParser
parameter_list|()
block|{              }
comment|/**      * Method for retrieving channel id from OpenChannelResponse message.      *       * @param in a InputStream with message      * @return channel id value      */
specifier|public
specifier|static
name|int
name|getChannelId
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|XMLStreamReader
name|streamReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
name|streamReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|streamReader
operator|.
name|next
argument_list|()
expr_stmt|;
name|int
name|eventType
init|=
name|streamReader
operator|.
name|getEventType
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventType
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
operator|&&
name|streamReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"openChannelResponse"
argument_list|)
condition|)
block|{
while|while
condition|(
name|streamReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|streamReader
operator|.
name|next
argument_list|()
expr_stmt|;
name|eventType
operator|=
name|streamReader
operator|.
name|getEventType
argument_list|()
expr_stmt|;
if|if
condition|(
name|eventType
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
operator|&&
name|streamReader
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"channelId"
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|streamReader
operator|.
name|getElementText
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|streamReader
argument_list|)
expr_stmt|;
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

