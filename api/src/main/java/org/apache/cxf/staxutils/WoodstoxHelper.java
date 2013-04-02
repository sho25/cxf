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
name|staxutils
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|stax
operator|.
name|WstxInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|XMLStreamReader2
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|final
class|class
name|WoodstoxHelper
block|{
specifier|private
name|WoodstoxHelper
parameter_list|()
block|{     }
specifier|public
specifier|static
name|XMLInputFactory
name|createInputFactory
parameter_list|()
block|{
return|return
operator|new
name|WstxInputFactory
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|setProperty
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|String
name|p
parameter_list|,
name|Object
name|v
parameter_list|)
block|{
operator|(
operator|(
name|XMLStreamReader2
operator|)
name|reader
operator|)
operator|.
name|setProperty
argument_list|(
name|p
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

