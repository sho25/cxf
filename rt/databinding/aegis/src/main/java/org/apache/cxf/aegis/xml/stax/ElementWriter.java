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
name|xml
operator|.
name|stax
package|;
end_package

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
name|XMLOutputFactory
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
name|util
operator|.
name|NamespaceHelper
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
name|AbstractMessageWriter
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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * LiteralWriter  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_class
specifier|public
class|class
name|ElementWriter
extends|extends
name|AbstractMessageWriter
implements|implements
name|MessageWriter
block|{
specifier|private
name|XMLStreamWriter
name|writer
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
comment|/**      * Create a LiteralWriter but without writing an element name.      *       * @param writer      */
specifier|public
name|ElementWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
block|}
specifier|public
name|ElementWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|this
argument_list|(
name|writer
argument_list|,
name|name
argument_list|,
name|namespace
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ElementWriter
parameter_list|(
name|XMLStreamWriter
name|streamWriter
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|streamWriter
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ElementWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
try|try
block|{
name|writeStartElement
argument_list|()
expr_stmt|;
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * @param os      * @throws XMLStreamException      */
specifier|public
name|ElementWriter
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLOutputFactory
name|ofactory
init|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|this
operator|.
name|writer
operator|=
name|ofactory
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
try|try
block|{
name|writeStartElement
argument_list|()
expr_stmt|;
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeStartElement
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|boolean
name|declare
init|=
literal|false
decl_stmt|;
name|String
name|decPrefix
init|=
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
comment|// If the user didn't specify a prefix, create one
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|prefix
argument_list|)
operator|&&
name|decPrefix
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|declare
operator|=
literal|true
expr_stmt|;
name|prefix
operator|=
name|NamespaceHelper
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
argument_list|)
condition|)
block|{
name|declare
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|prefix
operator|=
name|decPrefix
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|decPrefix
argument_list|)
condition|)
block|{
name|declare
operator|=
literal|true
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|name
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
if|if
condition|(
name|declare
condition|)
block|{
name|writer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValue(java.lang.Object)      */
specifier|public
name|void
name|writeValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|writeCharacters
argument_list|(
name|value
operator|.
name|toString
argument_list|()
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#getWriter(java.lang.String)      */
specifier|public
name|MessageWriter
name|getElementWriter
parameter_list|(
name|String
name|nm
parameter_list|)
block|{
return|return
operator|new
name|ElementWriter
argument_list|(
name|writer
argument_list|,
name|nm
argument_list|,
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|MessageWriter
name|getElementWriter
parameter_list|(
name|String
name|nm
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
return|return
operator|new
name|ElementWriter
argument_list|(
name|writer
argument_list|,
name|nm
argument_list|,
name|ns
argument_list|)
return|;
block|}
specifier|public
name|MessageWriter
name|getElementWriter
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
operator|new
name|ElementWriter
argument_list|(
name|writer
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getPrefix
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XMLStreamWriter
name|getXMLStreamWriter
parameter_list|()
block|{
return|return
name|writer
return|;
block|}
specifier|public
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|String
name|nm
parameter_list|)
block|{
return|return
operator|new
name|AttributeWriter
argument_list|(
name|writer
argument_list|,
name|nm
argument_list|,
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|String
name|nm
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
return|return
operator|new
name|AttributeWriter
argument_list|(
name|writer
argument_list|,
name|nm
argument_list|,
name|ns
argument_list|)
return|;
block|}
specifier|public
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
operator|new
name|AttributeWriter
argument_list|(
name|writer
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPrefixForNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
try|try
block|{
name|String
name|pfx
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|pfx
operator|==
literal|null
condition|)
block|{
name|pfx
operator|=
name|NamespaceHelper
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
return|return
name|prefix
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getPrefixForNamespace
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|hint
parameter_list|)
block|{
try|try
block|{
name|String
name|pfx
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|pfx
operator|==
literal|null
condition|)
block|{
name|String
name|ns2
init|=
name|writer
operator|.
name|getNamespaceContext
argument_list|()
operator|.
name|getNamespaceURI
argument_list|(
name|hint
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns2
operator|==
literal|null
condition|)
block|{
name|pfx
operator|=
name|hint
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ns2
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
return|return
name|pfx
return|;
block|}
else|else
block|{
name|pfx
operator|=
name|NamespaceHelper
operator|.
name|getUniquePrefix
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|setPrefix
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
return|return
name|pfx
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
literal|"Error writing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

