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
name|SOAPConstants
import|;
end_import

begin_comment
comment|/**  * Basic type conversion functionality for writing messages.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMessageWriter
implements|implements
name|MessageWriter
block|{
specifier|private
name|boolean
name|xsiTypeWritten
decl_stmt|;
specifier|public
name|AbstractMessageWriter
parameter_list|()
block|{     }
specifier|public
name|void
name|writeXsiType
parameter_list|(
name|QName
name|type
parameter_list|)
block|{
if|if
condition|(
name|xsiTypeWritten
condition|)
block|{
return|return;
block|}
name|xsiTypeWritten
operator|=
literal|true
expr_stmt|;
comment|/*          * Do not assume that the prefix supplied with the QName should be used          * in this case.          */
name|String
name|prefix
init|=
name|getPrefixForNamespace
argument_list|(
name|type
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|type
operator|.
name|getPrefix
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|+
name|type
operator|.
name|getLocalPart
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|type
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|type
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
name|getAttributeWriter
argument_list|(
literal|"type"
argument_list|,
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|)
operator|.
name|writeValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeXsiNil
parameter_list|()
block|{
name|MessageWriter
name|attWriter
init|=
name|getAttributeWriter
argument_list|(
literal|"nil"
argument_list|,
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|)
decl_stmt|;
name|attWriter
operator|.
name|writeValue
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|attWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsInt(java.lang.Integer)      */
specifier|public
name|void
name|writeValueAsInt
parameter_list|(
name|Integer
name|i
parameter_list|)
block|{
name|writeValue
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeValueAsByte
parameter_list|(
name|Byte
name|b
parameter_list|)
block|{
name|writeValue
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsDouble(java.lang.Double)      */
specifier|public
name|void
name|writeValueAsDouble
parameter_list|(
name|Double
name|d
parameter_list|)
block|{
name|writeValue
argument_list|(
name|d
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsCharacter(java.lang.Character)      */
specifier|public
name|void
name|writeValueAsCharacter
parameter_list|(
name|Character
name|char1
parameter_list|)
block|{
name|writeValue
argument_list|(
name|char1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsLong(java.lang.Long)      */
specifier|public
name|void
name|writeValueAsLong
parameter_list|(
name|Long
name|l
parameter_list|)
block|{
name|writeValue
argument_list|(
name|l
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsFloat(java.lang.Float)      */
specifier|public
name|void
name|writeValueAsFloat
parameter_list|(
name|Float
name|f
parameter_list|)
block|{
name|writeValue
argument_list|(
name|f
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cxf.aegis.xml.MessageWriter#writeValueAsBoolean(boolean)      */
specifier|public
name|void
name|writeValueAsBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|writeValue
argument_list|(
name|b
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeValueAsShort
parameter_list|(
name|Short
name|s
parameter_list|)
block|{
name|writeValue
argument_list|(
name|s
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

