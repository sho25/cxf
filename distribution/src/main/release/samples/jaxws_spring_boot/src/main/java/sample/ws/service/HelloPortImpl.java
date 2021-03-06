begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
operator|.
name|service
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

begin_comment
comment|/**  * Examples code for spring boot with CXF services. HelloPortImpl is the  * implementation for Hello interface. This class was generated by Apache CXF  * 3.1.0 2015-05-18T13:02:03.098-05:00 Generated source version: 3.1.0  */
end_comment

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
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
name|targetNamespace
operator|=
literal|"http://service.ws.sample/"
argument_list|,
name|endpointInterface
operator|=
literal|"sample.ws.service.Hello"
argument_list|)
specifier|public
class|class
name|HelloPortImpl
implements|implements
name|Hello
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|HelloPortImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|sayHello
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|myname
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation sayHello"
operator|+
name|myname
argument_list|)
expr_stmt|;
try|try
block|{
return|return
literal|"Hello, Welcome to CXF Spring boot "
operator|+
name|myname
operator|+
literal|"!!!"
return|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

