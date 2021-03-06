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
name|corba
operator|.
name|types
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|W3CConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TypeCode
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaOctetSequenceHandler
extends|extends
name|CorbaObjectHandler
block|{
specifier|private
name|byte
index|[]
name|value
decl_stmt|;
specifier|private
name|boolean
name|isBase64Octets
decl_stmt|;
specifier|public
name|CorbaOctetSequenceHandler
parameter_list|(
name|QName
name|primName
parameter_list|,
name|QName
name|primIdlType
parameter_list|,
name|TypeCode
name|primTC
parameter_list|,
name|Object
name|primType
parameter_list|)
block|{
name|super
argument_list|(
name|primName
argument_list|,
name|primIdlType
argument_list|,
name|primTC
argument_list|,
name|primType
argument_list|)
expr_stmt|;
name|isBase64Octets
operator|=
name|getType
argument_list|()
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|W3CConstants
operator|.
name|NT_SCHEMA_BASE64
argument_list|)
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|byte
index|[]
name|obj
parameter_list|)
block|{
name|value
operator|=
name|obj
expr_stmt|;
block|}
specifier|public
name|String
name|getDataFromValue
parameter_list|()
block|{
name|String
name|result
decl_stmt|;
if|if
condition|(
name|isBase64Octets
condition|)
block|{
name|result
operator|=
operator|new
name|String
argument_list|(
name|DatatypeConverter
operator|.
name|printBase64Binary
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|String
argument_list|(
name|DatatypeConverter
operator|.
name|printHexBinary
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|setValueFromData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|isBase64Octets
condition|)
block|{
name|value
operator|=
name|DatatypeConverter
operator|.
name|parseBase64Binary
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|DatatypeConverter
operator|.
name|parseHexBinary
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Not able to parse the octet sequence"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

