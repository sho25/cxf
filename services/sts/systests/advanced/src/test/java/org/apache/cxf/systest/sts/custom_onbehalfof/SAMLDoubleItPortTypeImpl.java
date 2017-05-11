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
name|sts
operator|.
name|custom_onbehalfof
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
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
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|endpointInterface
operator|=
literal|"org.example.contract.doubleit.DoubleItPortType"
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
name|SAMLDoubleItPortTypeImpl
implements|implements
name|DoubleItPortType
block|{
annotation|@
name|Resource
name|WebServiceContext
name|wsContext
decl_stmt|;
specifier|public
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
block|{
name|Principal
name|pr
init|=
name|wsContext
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Principal must not be null"
argument_list|,
name|pr
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Principal.getName() must not return null"
argument_list|,
name|pr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|pr
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|numberToDouble
operator|*
literal|2
return|;
block|}
block|}
end_class

end_unit

