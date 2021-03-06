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
name|exception
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"HelloService"
argument_list|,
name|portName
operator|=
literal|"HelloPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.cxf.systest.exception.GenericsEcho"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/test/HelloService"
argument_list|)
specifier|public
class|class
name|GenericsEchoImpl
block|{
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|request
parameter_list|)
throws|throws
name|GenericsException
block|{
name|GenericsException
name|exception
init|=
operator|new
name|GenericsException
argument_list|()
decl_stmt|;
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
name|objs
init|=
operator|new
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|100
argument_list|)
argument_list|)
decl_stmt|;
name|exception
operator|.
name|setObj
argument_list|(
name|objs
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
end_class

end_unit

