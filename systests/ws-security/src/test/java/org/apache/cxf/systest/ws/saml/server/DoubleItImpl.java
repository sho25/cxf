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
name|ws
operator|.
name|saml
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|cxf
operator|.
name|feature
operator|.
name|Features
import|;
end_import

begin_import
import|import
name|wssec
operator|.
name|saml
operator|.
name|DoubleItPortType
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://WSSec/saml"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|endpointInterface
operator|=
literal|"wssec.saml.DoubleItPortType"
argument_list|)
annotation|@
name|Features
argument_list|(
name|features
operator|=
literal|"org.apache.cxf.feature.LoggingFeature"
argument_list|)
specifier|public
class|class
name|DoubleItImpl
implements|implements
name|DoubleItPortType
block|{
specifier|public
name|java
operator|.
name|math
operator|.
name|BigInteger
name|doubleIt
parameter_list|(
name|java
operator|.
name|math
operator|.
name|BigInteger
name|numberToDouble
parameter_list|)
block|{
return|return
name|numberToDouble
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

