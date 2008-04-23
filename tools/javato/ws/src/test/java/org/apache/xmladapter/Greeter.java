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
name|xmladapter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Currency
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
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

begin_comment
comment|// import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_interface
annotation|@
name|WebService
specifier|public
interface|interface
name|Greeter
block|{
name|int
name|sayHi
parameter_list|(
annotation|@
name|WebParam
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|value
operator|=
name|org
operator|.
name|apache
operator|.
name|xmladapter
operator|.
name|CurrencyAdapter
operator|.
name|class
argument_list|)
name|Currency
name|tickerSymbol
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

