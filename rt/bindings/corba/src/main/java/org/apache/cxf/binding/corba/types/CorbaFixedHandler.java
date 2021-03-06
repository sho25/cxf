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
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|wsdl
operator|.
name|Anonfixed
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
name|Fixed
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
specifier|final
class|class
name|CorbaFixedHandler
extends|extends
name|CorbaObjectHandler
block|{
specifier|private
specifier|final
name|long
name|digits
decl_stmt|;
specifier|private
specifier|final
name|long
name|scale
decl_stmt|;
specifier|private
name|BigDecimal
name|value
decl_stmt|;
specifier|public
name|CorbaFixedHandler
parameter_list|(
name|QName
name|fixedName
parameter_list|,
name|QName
name|fixedIdlType
parameter_list|,
name|TypeCode
name|fixedTC
parameter_list|,
name|Object
name|fixedType
parameter_list|)
block|{
name|super
argument_list|(
name|fixedName
argument_list|,
name|fixedIdlType
argument_list|,
name|fixedTC
argument_list|,
name|fixedType
argument_list|)
expr_stmt|;
if|if
condition|(
name|fixedType
operator|instanceof
name|Fixed
condition|)
block|{
name|digits
operator|=
operator|(
operator|(
name|Fixed
operator|)
name|fixedType
operator|)
operator|.
name|getDigits
argument_list|()
expr_stmt|;
name|scale
operator|=
operator|(
operator|(
name|Fixed
operator|)
name|fixedType
operator|)
operator|.
name|getScale
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fixedType
operator|instanceof
name|Anonfixed
condition|)
block|{
name|digits
operator|=
operator|(
operator|(
name|Anonfixed
operator|)
name|fixedType
operator|)
operator|.
name|getDigits
argument_list|()
expr_stmt|;
name|scale
operator|=
operator|(
operator|(
name|Anonfixed
operator|)
name|fixedType
operator|)
operator|.
name|getScale
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// This should never happen
name|digits
operator|=
literal|0
expr_stmt|;
name|scale
operator|=
literal|0
expr_stmt|;
block|}
block|}
specifier|public
name|long
name|getDigits
parameter_list|()
block|{
return|return
name|digits
return|;
block|}
specifier|public
name|long
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
specifier|public
name|BigDecimal
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|String
name|getValueData
parameter_list|()
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|BigDecimal
name|val
parameter_list|)
block|{
name|value
operator|=
name|val
expr_stmt|;
block|}
specifier|public
name|void
name|setValueFromData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|value
operator|=
operator|new
name|BigDecimal
argument_list|(
name|data
argument_list|)
expr_stmt|;
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

