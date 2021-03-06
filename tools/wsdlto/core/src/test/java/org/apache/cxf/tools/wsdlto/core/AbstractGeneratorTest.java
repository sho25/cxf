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
name|wsdlto
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
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
name|tools
operator|.
name|util
operator|.
name|FileWriterUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractGeneratorTest
extends|extends
name|ProcessorTestBase
block|{
name|DummyGenerator
name|gen
decl_stmt|;
name|ToolContext
name|context
decl_stmt|;
name|FileWriterUtil
name|util
decl_stmt|;
name|String
name|packageName
init|=
literal|"org.apache"
decl_stmt|;
name|String
name|className
init|=
literal|"Hello"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testKeep
parameter_list|()
throws|throws
name|Exception
block|{
name|gen
operator|=
operator|new
name|DummyGenerator
argument_list|()
expr_stmt|;
name|util
operator|=
operator|new
name|FileWriterUtil
argument_list|(
name|output
operator|.
name|toString
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|ToolContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setEnvironment
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Writer
name|writer
init|=
name|util
operator|.
name|getWriter
argument_list|(
name|packageName
argument_list|,
name|className
operator|+
literal|".java"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_NEW_ONLY
argument_list|,
literal|"keep"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|gen
operator|.
name|parseOutputName
argument_list|(
name|packageName
argument_list|,
name|className
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverwrite
parameter_list|()
throws|throws
name|Exception
block|{
name|gen
operator|=
operator|new
name|DummyGenerator
argument_list|()
expr_stmt|;
name|util
operator|=
operator|new
name|FileWriterUtil
argument_list|(
name|output
operator|.
name|toString
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|ToolContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setEnvironment
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Writer
name|writer
init|=
name|util
operator|.
name|getWriter
argument_list|(
name|packageName
argument_list|,
name|className
operator|+
literal|".java"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|gen
operator|.
name|parseOutputName
argument_list|(
name|packageName
argument_list|,
name|className
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

