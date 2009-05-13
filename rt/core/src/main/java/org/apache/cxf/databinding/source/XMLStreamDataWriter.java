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
name|databinding
operator|.
name|source
package|;
end_package

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
name|util
operator|.
name|Collection
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
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|DocumentFragment
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
name|Node
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
name|i18n
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|databinding
operator|.
name|DataWriter
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
name|interceptor
operator|.
name|Fault
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
name|Attachment
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|W3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|XMLStreamDataWriter
implements|implements
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|XMLStreamDataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
name|write
argument_list|(
name|obj
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
try|try
block|{
name|XMLStreamReader
name|reader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|DataSource
condition|)
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|obj
decl_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Node
condition|)
block|{
name|Node
name|nd
init|=
operator|(
name|Node
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|writer
operator|instanceof
name|W3CDOMStreamWriter
operator|&&
operator|(
operator|(
name|W3CDOMStreamWriter
operator|)
name|writer
operator|)
operator|.
name|getCurrentNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|W3CDOMStreamWriter
name|dw
init|=
operator|(
name|W3CDOMStreamWriter
operator|)
name|writer
decl_stmt|;
if|if
condition|(
name|nd
operator|.
name|getOwnerDocument
argument_list|()
operator|==
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
condition|)
block|{
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|nd
operator|instanceof
name|DocumentFragment
condition|)
block|{
name|nd
operator|=
name|dw
operator|.
name|getDocument
argument_list|()
operator|.
name|importNode
argument_list|(
name|nd
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|nd
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Source
name|s
init|=
operator|(
name|Source
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|s
operator|instanceof
name|DOMSource
operator|&&
operator|(
operator|(
name|DOMSource
operator|)
name|s
operator|)
operator|.
name|getNode
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|StaxUtils
operator|.
name|copy
argument_list|(
name|s
argument_list|,
name|writer
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
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_READ_XML_STREAM"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{     }
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{      }
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{     }
block|}
end_class

end_unit

