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
name|aegis
operator|.
name|type
operator|.
name|xml
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementReader
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementWriter
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
name|FragmentStreamReader
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

begin_comment
comment|/**  * Reads and writes<code>org.w3c.dom.Document</code> types.  */
end_comment

begin_class
specifier|public
class|class
name|DocumentType
extends|extends
name|AegisType
block|{
specifier|public
name|DocumentType
parameter_list|()
block|{
name|setWriteOuter
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|mreader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
try|try
block|{
name|XMLStreamReader
name|reader
init|=
operator|(
operator|(
name|ElementReader
operator|)
name|mreader
operator|)
operator|.
name|getXMLStreamReader
argument_list|()
decl_stmt|;
comment|// we need to eat the surrounding element.
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|Object
name|tree
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
literal|null
argument_list|,
operator|new
name|FragmentStreamReader
argument_list|(
name|reader
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|reader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|// eat the end tag.
return|return
name|tree
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not parse xml."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Document
name|doc
init|=
operator|(
name|Document
operator|)
name|object
decl_stmt|;
try|try
block|{
name|Element
name|docElement
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|docElement
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isNillable
argument_list|()
condition|)
block|{
name|writer
operator|.
name|writeXsiNil
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not write xml: null document element."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|StaxUtils
operator|.
name|writeElement
argument_list|(
name|docElement
argument_list|,
operator|(
operator|(
name|ElementWriter
operator|)
name|writer
operator|)
operator|.
name|getXMLStreamWriter
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not write xml."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

