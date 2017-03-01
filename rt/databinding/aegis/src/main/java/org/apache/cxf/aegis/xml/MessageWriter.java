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

begin_comment
comment|/**  * Writes messages to an output stream.  */
end_comment

begin_interface
specifier|public
interface|interface
name|MessageWriter
block|{
name|void
name|writeValue
parameter_list|(
name|Object
name|value
parameter_list|)
function_decl|;
name|void
name|writeValueAsInt
parameter_list|(
name|Integer
name|i
parameter_list|)
function_decl|;
name|void
name|writeValueAsCharacter
parameter_list|(
name|Character
name|char1
parameter_list|)
function_decl|;
name|void
name|writeValueAsDouble
parameter_list|(
name|Double
name|double1
parameter_list|)
function_decl|;
name|void
name|writeValueAsLong
parameter_list|(
name|Long
name|l
parameter_list|)
function_decl|;
name|void
name|writeValueAsFloat
parameter_list|(
name|Float
name|f
parameter_list|)
function_decl|;
name|void
name|writeValueAsShort
parameter_list|(
name|Short
name|short1
parameter_list|)
function_decl|;
name|void
name|writeValueAsByte
parameter_list|(
name|Byte
name|b
parameter_list|)
function_decl|;
name|void
name|writeValueAsBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|)
function_decl|;
name|MessageWriter
name|getAttributeWriter
parameter_list|(
name|QName
name|qname
parameter_list|)
function_decl|;
name|MessageWriter
name|getElementWriter
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|MessageWriter
name|getElementWriter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|namespace
parameter_list|)
function_decl|;
name|MessageWriter
name|getElementWriter
parameter_list|(
name|QName
name|qname
parameter_list|)
function_decl|;
name|String
name|getPrefixForNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
function_decl|;
comment|/**      * Get a prefix for a namespace. After calling this, the prefix returned is      * registered with the namespace.<p/> This method will make an attempt to      * use the hint prefix if possible. If the namespace is already registered      * or the hint is already registered with a different namespace then the      * behavior will be the same as the non-hint version.      *      * @param namespace the namespace to retrieve the prefix for      * @param hint the hint for the prefix.      * @return the prefix associated with the namespace      */
name|String
name|getPrefixForNamespace
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|hint
parameter_list|)
function_decl|;
comment|/**      * Tells the MessageWriter that writing operations are completed so it can      * write the end element.      */
name|void
name|close
parameter_list|()
function_decl|;
comment|/**      * As per<a href="http://www.w3.org/TR/xmlschema-1/#xsi_type">2.6.1</a> in      * XML Schema Part 1: "An element information item in an instance may,      * however, explicitly assert its type using the attribute      *<code>xsi:type</code>."      *      * @param type the QName of the type being referenced.      */
name|void
name|writeXsiType
parameter_list|(
name|QName
name|qn
parameter_list|)
function_decl|;
name|void
name|writeXsiNil
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

