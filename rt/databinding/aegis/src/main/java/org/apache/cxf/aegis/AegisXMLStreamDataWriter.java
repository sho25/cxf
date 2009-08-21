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
package|;
end_package

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
name|type
operator|.
name|Type
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
name|TypeUtil
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

begin_class
specifier|public
class|class
name|AegisXMLStreamDataWriter
extends|extends
name|AbstractAegisIoImpl
implements|implements
name|AegisWriter
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
name|AegisXMLStreamDataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|AegisXMLStreamDataWriter
parameter_list|(
name|AegisContext
name|globalContext
parameter_list|)
block|{
name|super
argument_list|(
name|globalContext
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write an object to the output. This method always writes xsi:type attributes.      * @param obj The object to write.      * @param elementName the QName of the XML Element.      * @param optional set this for minOccurs = 0. It omits null elements.      * @param output the output stream      * @param aegisType the aegis type.      * @throws Exception      */
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|QName
name|elementName
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|,
name|Type
name|aegisType
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|obj
operator|==
literal|null
operator|&&
name|aegisType
operator|==
literal|null
operator|&&
operator|!
name|optional
condition|)
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
literal|"WRITE_NEEDS_TYPE"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|DatabindingException
argument_list|(
name|message
argument_list|)
throw|;
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|aegisType
operator|==
literal|null
condition|)
block|{
name|aegisType
operator|=
name|TypeUtil
operator|.
name|getWriteType
argument_list|(
name|aegisContext
argument_list|,
name|obj
argument_list|,
name|aegisType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|optional
condition|)
block|{
comment|// minOccurs = 0
return|return;
block|}
if|if
condition|(
name|aegisType
operator|.
name|isNillable
argument_list|()
operator|&&
name|aegisType
operator|.
name|isWriteOuter
argument_list|()
condition|)
block|{
name|ElementWriter
name|writer
init|=
operator|new
name|ElementWriter
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|MessageWriter
name|w2
init|=
name|writer
operator|.
name|getElementWriter
argument_list|(
name|elementName
argument_list|)
decl_stmt|;
name|w2
operator|.
name|writeXsiNil
argument_list|()
expr_stmt|;
name|w2
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|ElementWriter
name|writer
init|=
operator|new
name|ElementWriter
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|MessageWriter
name|w2
init|=
name|writer
operator|.
name|getElementWriter
argument_list|(
name|elementName
argument_list|)
decl_stmt|;
if|if
condition|(
name|getContext
argument_list|()
operator|.
name|isWriteXsiTypes
argument_list|()
operator|&&
name|aegisType
operator|!=
literal|null
operator|&&
name|aegisType
operator|.
name|getSchemaType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if we know the type, write it. We are standalone, and the reader needs it.
name|w2
operator|.
name|writeXsiType
argument_list|(
name|aegisType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|aegisType
operator|.
name|writeObject
argument_list|(
name|obj
argument_list|,
name|w2
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|w2
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

