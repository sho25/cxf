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
name|schema_validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|SchemaValidation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|ComplexStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|OccuringStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeRequestWithHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|schema_validation
operator|.
name|types
operator|.
name|SomeResponseWithHeader
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SchemaValidationService"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.schema_validation.SchemaValidation"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/schema_validation"
argument_list|)
annotation|@
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|SchemaValidation
specifier|public
class|class
name|SchemaValidationImpl
implements|implements
name|SchemaValidation
block|{
specifier|public
name|boolean
name|setComplexStruct
parameter_list|(
name|ComplexStruct
name|in
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|setOccuringStruct
parameter_list|(
name|OccuringStruct
name|in
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|ComplexStruct
name|getComplexStruct
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|ComplexStruct
name|complexStruct
init|=
operator|new
name|ComplexStruct
argument_list|()
decl_stmt|;
name|complexStruct
operator|.
name|setElem1
argument_list|(
name|in
operator|+
literal|"-one"
argument_list|)
expr_stmt|;
comment|// Don't initialize a member of the structure.  Validation should throw
comment|// an exception.
comment|// complexStruct.setElem2(in + "-two");
name|complexStruct
operator|.
name|setElem3
argument_list|(
name|in
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|complexStruct
return|;
block|}
specifier|public
name|OccuringStruct
name|getOccuringStruct
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|OccuringStruct
name|occuringStruct
init|=
operator|new
name|OccuringStruct
argument_list|()
decl_stmt|;
comment|// Populate the list in the wrong order.  Validation should throw
comment|// an exception.
name|List
argument_list|<
name|Serializable
argument_list|>
name|floatIntStringList
init|=
name|occuringStruct
operator|.
name|getVarFloatAndVarIntAndVarString
argument_list|()
decl_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
name|in
operator|+
literal|"-two"
argument_list|)
expr_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|floatIntStringList
operator|.
name|add
argument_list|(
operator|new
name|Float
argument_list|(
literal|2.5f
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|occuringStruct
return|;
block|}
annotation|@
name|Override
specifier|public
name|SomeResponse
name|doSomething
parameter_list|(
name|SomeRequest
name|in
parameter_list|)
block|{
name|SomeResponse
name|response
init|=
operator|new
name|SomeResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"1234567890"
argument_list|)
condition|)
block|{
name|response
operator|.
name|setTransactionId
argument_list|(
literal|"aaaaaaaaaaxxx"
argument_list|)
expr_stmt|;
comment|// invalid transaction id
block|}
else|else
block|{
name|response
operator|.
name|setTransactionId
argument_list|(
literal|"aaaaaaaaaa"
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|public
name|SomeResponseWithHeader
name|doSomethingWithHeader
parameter_list|(
name|SomeRequestWithHeader
name|in
parameter_list|,
name|SomeHeader
name|inHeader
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

