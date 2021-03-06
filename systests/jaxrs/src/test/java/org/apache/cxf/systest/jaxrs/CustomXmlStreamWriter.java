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
name|systest
operator|.
name|jaxrs
package|;
end_package

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
name|XMLStreamWriter
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
name|DelegatingXMLStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|CustomXmlStreamWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|public
name|CustomXmlStreamWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|super
operator|.
name|writeStartElement
argument_list|(
literal|"b"
argument_list|,
name|local
argument_list|,
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"Book"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
condition|)
block|{
name|super
operator|.
name|writeNamespace
argument_list|(
literal|"b"
argument_list|,
literal|"http://www.w3.org/1999/xhtml"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

