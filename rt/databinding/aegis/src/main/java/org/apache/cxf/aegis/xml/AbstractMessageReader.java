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
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * Basic type conversions for reading messages.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMessageReader
implements|implements
name|MessageReader
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|XSI_NIL
init|=
operator|new
name|QName
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSI
argument_list|,
literal|"nil"
argument_list|,
literal|"xsi"
argument_list|)
decl_stmt|;
specifier|public
name|AbstractMessageReader
parameter_list|()
block|{     }
specifier|public
name|void
name|readToEnd
parameter_list|()
block|{
name|readToEnd
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readToEnd
parameter_list|(
name|MessageReader
name|childReader
parameter_list|)
block|{
while|while
condition|(
name|childReader
operator|.
name|hasMoreElementReaders
argument_list|()
condition|)
block|{
name|readToEnd
argument_list|(
name|childReader
operator|.
name|getNextElementReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isXsiNil
parameter_list|()
block|{
name|MessageReader
name|nilReader
init|=
name|getAttributeReader
argument_list|(
name|XSI_NIL
argument_list|)
decl_stmt|;
name|boolean
name|nil
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|nilReader
operator|!=
literal|null
condition|)
block|{
name|String
name|value
init|=
name|nilReader
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|(
literal|"true"
operator|.
name|equals
argument_list|(
name|value
operator|.
name|trim
argument_list|()
argument_list|)
operator|||
literal|"1"
operator|.
name|equals
argument_list|(
name|value
operator|.
name|trim
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|nil
return|;
block|}
specifier|public
name|boolean
name|hasValue
parameter_list|()
block|{
return|return
name|getValue
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageReader#getValueAsCharacter()      */
specifier|public
name|char
name|getValueAsCharacter
parameter_list|()
block|{
if|if
condition|(
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|getValue
argument_list|()
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|public
name|int
name|getValueAsInt
parameter_list|()
block|{
if|if
condition|(
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageReader#getValueAsLong()      */
specifier|public
name|long
name|getValueAsLong
parameter_list|()
block|{
if|if
condition|(
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0L
return|;
block|}
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageReader#getValueAsDouble()      */
specifier|public
name|double
name|getValueAsDouble
parameter_list|()
block|{
if|if
condition|(
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0d
return|;
block|}
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageReader#getValueAsFloat()      */
specifier|public
name|float
name|getValueAsFloat
parameter_list|()
block|{
if|if
condition|(
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0f
return|;
block|}
return|return
name|Float
operator|.
name|parseFloat
argument_list|(
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageReader#getValueAsBoolean()      */
specifier|public
name|boolean
name|getValueAsBoolean
parameter_list|()
block|{
name|String
name|value
init|=
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
operator|||
literal|"1"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
literal|"false"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
operator|||
literal|"0"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Invalid boolean value: "
operator|+
name|value
argument_list|)
throw|;
block|}
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

