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
name|staxutils
package|;
end_package

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
name|stream
operator|.
name|StreamFilter
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

begin_class
specifier|public
class|class
name|StaxStreamFilter
implements|implements
name|StreamFilter
block|{
specifier|private
name|QName
index|[]
name|tags
decl_stmt|;
specifier|public
name|StaxStreamFilter
parameter_list|(
name|QName
modifier|...
name|eventsToReject
parameter_list|)
block|{
name|tags
operator|=
name|eventsToReject
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
if|if
condition|(
name|reader
operator|.
name|isStartElement
argument_list|()
condition|)
block|{
name|QName
name|elName
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|QName
name|tag
range|:
name|tags
control|)
block|{
if|if
condition|(
name|elName
operator|.
name|equals
argument_list|(
name|tag
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|reader
operator|.
name|isEndElement
argument_list|()
condition|)
block|{
name|QName
name|elName
init|=
name|reader
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|QName
name|tag
range|:
name|tags
control|)
block|{
if|if
condition|(
name|elName
operator|.
name|equals
argument_list|(
name|tag
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

