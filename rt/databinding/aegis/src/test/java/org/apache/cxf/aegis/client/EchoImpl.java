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
name|client
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_class
specifier|public
class|class
name|EchoImpl
implements|implements
name|Echo
block|{
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|text
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|textAgain
parameter_list|)
block|{
name|textAgain
operator|.
name|value
operator|=
literal|"header2"
expr_stmt|;
return|return
name|text
return|;
block|}
block|}
end_class

end_unit

