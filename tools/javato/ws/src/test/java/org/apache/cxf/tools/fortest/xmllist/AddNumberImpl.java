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
name|tools
operator|.
name|fortest
operator|.
name|xmllist
package|;
end_package

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

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.tools.fortest.xmllist.AddNumbersPortType"
argument_list|)
specifier|public
class|class
name|AddNumberImpl
implements|implements
name|AddNumbersPortType
block|{
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|addNumbers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|arg
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|UserObject
name|testCXF1752
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|receivers
parameter_list|,
name|UserObject
name|item
parameter_list|,
name|byte
index|[]
name|binaryContent
parameter_list|,
name|UserObject
index|[]
name|objects
parameter_list|,
name|List
argument_list|<
name|UserObject
argument_list|>
name|objects2
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

